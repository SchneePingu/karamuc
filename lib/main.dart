// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:convert';
import 'package:flutter/services.dart';

import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

void main() {
  runApp(const BookingApplication());
}

class BookingSlotsViewFactory {
  final DailyBookings dailyBookings;

  BookingSlotsViewFactory({required this.dailyBookings});

  BookingSlotsView buildBookingSlotView(String date, String roomSize) {
    final roomBookings = dailyBookings.getRoomBookings(date);

    List<BookingSlot> bookingSlots;

    if (roomBookings == null) {
      bookingSlots = [];
    } else {
      bookingSlots = roomBookings.getBookingSlots(roomSize);
    }

    return BookingSlotsView(
        bookingSlots: bookingSlots, hasBookingSlots: roomBookings != null);
  }
}

class BookingSlotsView extends StatelessWidget {
  final List<BookingSlot> bookingSlots;
  final bool hasBookingSlots;

  const BookingSlotsView(
      {super.key, required this.bookingSlots, required this.hasBookingSlots});

  @override
  Widget build(BuildContext context) {
    return hasBookingSlots
        ? bookingSlots.isNotEmpty
            ? ListView.separated(
                padding: const EdgeInsets.all(8),
                itemCount: bookingSlots.length,
                itemBuilder: (BuildContext context, int index) {
                  return Container(
                    height: 50,
                    color: Colors.blue,
                    child: Center(
                        child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                          SizedBox(
                              width: 130,
                              child: Text(
                                  '${bookingSlots[index].from} - ${bookingSlots[index].until}',
                                  style: const TextStyle(
                                      color: Colors.white, fontSize: 16))),
                          SizedBox(
                              width: 100,
                              child: Text('${bookingSlots[index].price} Euro',
                                  style: const TextStyle(
                                      color: Colors.white, fontSize: 16))),
                          SizedBox(
                              width: 110,
                              child: Text(bookingSlots[index].roomName,
                                  style: const TextStyle(
                                      color: Colors.white, fontSize: 16))),
                        ])),
                  );
                },
                separatorBuilder: (BuildContext context, int index) =>
                    const Divider(),
              )
            : const Center(
                child: Text("Ausgebucht",
                    style: TextStyle(fontSize: 22, color: Colors.blueGrey)))
        : const Center(
            child: Text(
            "Reservierungen für diesen Tag\nkonnten nicht abgerufen werden.\n:(",
            style: TextStyle(fontSize: 20, color: Colors.blueGrey),
            textAlign: TextAlign.center,
          ));
  }

  String showBookingSlot(BookingSlot bookingSlot) {
    return '${bookingSlot.roomName}  |  ${bookingSlot.from} - ${bookingSlot.until}  |  ${bookingSlot.price} Euro';
  }
}

class BookingData {
  List<Date> dates;
  List<String> roomSizes;
  DailyBookings dailyBookings;

  BookingData(
      {required this.dates,
      required this.roomSizes,
      required this.dailyBookings});

  factory BookingData.fromJson(Map<String, dynamic> json) {
    return BookingData(
        roomSizes: (json['roomSizes'] as List<dynamic>)
            .map((roomSize) => roomSize as String)
            .toList(),
        dates: (json['dates'] as List<dynamic>)
            .map((date) => Date.fromJson(date as Map<String, dynamic>))
            .toList(),
        dailyBookings: DailyBookings.fromJson(json['bookings']));
  }
}

class Date {
  String date;
  String weekday;

  Date({required this.date, required this.weekday});

  factory Date.fromJson(Map<String, dynamic> json) {
    return Date(
        date: json['date'] as String, weekday: json['weekday'] as String);
  }
}

class DailyBookings {
  Map<String, RoomBookings> data;

  DailyBookings({required this.data});

  factory DailyBookings.fromJson(Map<String, dynamic> json) {
    return DailyBookings(
        data: json.map((date, roomBooking) =>
            MapEntry(date, RoomBookings.fromJson(roomBooking))));
  }

  RoomBookings? getRoomBookings(String date) {
    return data[date];
  }
}

class RoomBookings {
  Map<String, List<BookingSlot>> data;

  RoomBookings({required this.data});

  factory RoomBookings.fromJson(Map<String, dynamic> json) {
    return RoomBookings(
        data: json.map((roomSize, bookingSlots) => MapEntry(
            roomSize,
            (bookingSlots as List)
                .map((bookingSlot) => BookingSlot.fromJson(bookingSlot))
                .toList())));
  }

  List<BookingSlot> getBookingSlots(String roomSize) {
    return data[roomSize] ?? [];
  }
}

class BookingSlot {
  String from;
  String until;
  int price;
  String roomName;

  BookingSlot(
      {required this.from,
      required this.until,
      required this.price,
      required this.roomName});

  factory BookingSlot.fromJson(Map<String, dynamic> json) {
    return BookingSlot(
        from: json['from'] as String,
        until: json['until'] as String,
        price: json['price'] as int,
        roomName: json['roomName'] as String);
  }
}

class BookingWidget extends StatefulWidget {
  const BookingWidget({super.key});

  @override
  State<BookingWidget> createState() => _BookingWidgetState();
}

class _BookingWidgetState extends State<BookingWidget> {
  DateTime selectedDate = getNextWorkingDay();
  String selectedDateDisplayValue = "Datum wählen";

  String selectedRoomSize = "";

  bool isDateSelected = false;
  bool isLoading = false;

  BookingData bookingData = BookingData(
      dates: [], roomSizes: [], dailyBookings: DailyBookings(data: {}));

  static const platform = MethodChannel('karamuclib');

  static DateTime getNextWorkingDay() {
    var today = DateTime.now();
    var currentWeekDay = today.weekday;

    if (currentWeekDay >= 3 && currentWeekDay <= 6) {
      return today;
    }

    return today.add(Duration(days: 3 - (currentWeekDay % 7)));
  }

  String showDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}.${date.month.toString().padLeft(2, '0')}.${date.year}';
  }

  int getInitialDateTabIndex() {
    var index = bookingData.dates
        .indexWhere((date) => date.date == selectedDateDisplayValue);
    return index != -1 ? index : 0;
  }

  List<Tab> getDateTabs() {
    return bookingData.dates
        .map((date) =>
            Tab(text: '${date.weekday}\n${date.date.substring(0, 6)}'))
        .toList();
  }

  List<DropdownMenuItem<String>> getRoomSizeDropdownMenuItems() {
    return bookingData.roomSizes
        .map((roomSize) =>
            DropdownMenuItem(value: roomSize, child: Text(roomSize)))
        .toList();
  }

  List<BookingSlotsView> getBookingSlotsViews() {
    var factory =
        BookingSlotsViewFactory(dailyBookings: bookingData.dailyBookings);

    return bookingData.dates
        .map(
            (date) => factory.buildBookingSlotView(date.date, selectedRoomSize))
        .toList();
  }

  Future<BookingData> fetchBookingData(String date) async {
    return platform
        .invokeMethod('fetchBookingData', date)
        .then((jsonString) => BookingData.fromJson(json.decode(jsonString)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: Column(children: [
        const SizedBox(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            SizedBox(
                height: 40,
                width: 200,
                child: ElevatedButton.icon(
                  icon: const Icon(
                    Icons.calendar_month,
                  ),
                  label: Text(selectedDateDisplayValue),
                  onPressed: isLoading
                      ? null
                      : () async {
                          final preSelectedDate = selectedDate;

                          final newSelectedDate = await showDatePicker(
                              firstDate: DateTime.now(),
                              lastDate:
                                  DateTime.now().add(const Duration(days: 365)),
                              context: context,
                              locale: const Locale("de", "DE"),
                              initialDate: preSelectedDate,
                              selectableDayPredicate: (DateTime date) =>
                                  date.weekday >= 3 && date.weekday <= 6);

                          if (newSelectedDate == null) {
                            return;
                          }

                          final newSelectedDateDisplayValue =
                              showDate(newSelectedDate);

                          setState(() {
                            selectedDate = newSelectedDate;
                            selectedDateDisplayValue =
                                newSelectedDateDisplayValue;
                            isDateSelected = true;
                            isLoading = true;
                          });

                          fetchBookingData(newSelectedDateDisplayValue)
                              .then((newBookingData) => () {
                                    var newSelectedRoomSize =
                                        newBookingData.roomSizes.isNotEmpty
                                            ? newBookingData.roomSizes.first
                                            : "";
                                    if (newBookingData.roomSizes.any(
                                        (roomSize) =>
                                            roomSize == selectedRoomSize)) {
                                      newSelectedRoomSize = selectedRoomSize;
                                    }

                                    setState(() {
                                      bookingData = newBookingData;
                                      selectedRoomSize = newSelectedRoomSize;
                                    });
                                  }())
                              .whenComplete(() => setState(() {
                                    isLoading = false;
                                  }));
                        },
                )),
            SizedBox(
                width: 80,
                child: (isDateSelected &&
                        !isLoading &&
                        bookingData.roomSizes.isNotEmpty
                    ? Row(children: [
                        DropdownButton<String>(
                          dropdownColor: Colors.blueGrey,
                          style: const TextStyle(
                              color: Colors.white, fontSize: 20),
                          value: selectedRoomSize,
                          items: getRoomSizeDropdownMenuItems(),
                          onChanged: (String? newSelectedRoomSize) {
                            if (newSelectedRoomSize == null) {
                              return;
                            }

                            setState(() {
                              selectedRoomSize = newSelectedRoomSize;
                            });
                          },
                        ),
                        const Icon(Icons.person, color: Colors.orange)
                      ])
                    : null)),
          ],
        ),
        const SizedBox(
          height: 10,
        )
      ])),
      body: (isDateSelected
          ? isLoading
              ? Container(
                  alignment: Alignment.center,
                  child: SizedBox(
                      width: 200,
                      height: 200,
                      child: Column(children: [
                        const SizedBox(
                            width: 100,
                            height: 100,
                            child: CircularProgressIndicator(
                              color: Colors.blueAccent,
                              strokeWidth: 5,
                            )),
                        Container(height: 30),
                        const Text("Abwarten und Spezi trinken.",
                            textAlign: TextAlign.center,
                            style:
                                TextStyle(fontSize: 16, color: Colors.blueGrey))
                      ])))
              : bookingData.dailyBookings.data.isEmpty
                  ? const Center(
                      child: Text(
                          "\"München, wir haben ein Problem!\"\n-\nReservierungen konnten\nnicht abgerufen werden.\n\n\nBist Du mit dem\nInternet verbunden?\n;)",
                          textAlign: TextAlign.center,
                          style:
                              TextStyle(fontSize: 20, color: Colors.blueGrey)))
                  : DefaultTabController(
                      length: bookingData.dates.length,
                      initialIndex: getInitialDateTabIndex(),
                      child: Column(children: [
                        SizedBox(
                            height: 50,
                            child: AppBar(
                              bottom: TabBar(
                                tabs: getDateTabs(),
                              ),
                            )),
                        Expanded(
                            child: TabBarView(
                          children: getBookingSlotsViews(),
                        ))
                      ]))
          : null),
    );
  }
}

class BookingApplication extends StatelessWidget {
  const BookingApplication({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: const BookingWidget(),
      theme: ThemeData.dark(),
      localizationsDelegates: const [GlobalMaterialLocalizations.delegate],
      supportedLocales: const [
        Locale('de'),
      ],
    );
  }
}

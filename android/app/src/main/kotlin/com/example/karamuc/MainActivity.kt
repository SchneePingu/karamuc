package com.example.karamuc

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import lib.Lib

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.getDartExecutor().getBinaryMessenger(),
            MainActivity.Companion.CHANNEL
        )
            .setMethodCallHandler { call, result ->
                if (call.method.equals("fetchBookingData")) {
                    result.success(Lib.fetchBookingData(call.arguments.toString()))
                } else {
                    result.notImplemented()
                }
            }
    }

    companion object {
        private const val CHANNEL = "karamuclib"
    }
}

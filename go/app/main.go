package main

import (
    "fmt"
    "github.com/SchneePingu/karamuc/lib"
)

func main() {
    date := "01.06.2023"
    fmt.Println(lib.FetchBookingData(date))
}

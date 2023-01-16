package lib

import (
    "errors"
    "fmt"
    "io"
    "net/http"
    "net/url"
    "strings"
)


const BOOKING_WEBSITE_URL = "https://www.yokochokaraoke.de/buchen"

func requestBookingWebsite(date string) (*http.Response, error) {
    request := buildRequest(date)

    return sendRequest(request)
}

func buildRequest(date string) *http.Request {
    requestBody := buildRequestBody(date)

    request, _ := http.NewRequest(http.MethodPost, BOOKING_WEBSITE_URL,
        requestBody)
    request.Header.Add("Content-Type", "application/x-www-form-urlencoded")

    return request
}

func buildRequestBody(date string) io.Reader {
    urlParameters := buildUrlParameters(date)

    return strings.NewReader(urlParameters.Encode())
}

func buildUrlParameters(date string) url.Values {
    urlParameters := url.Values{}

    urlParameters.Set("product_filter_date", date)

    return urlParameters
}

func sendRequest(request *http.Request) (*http.Response, error) {
    client := &http.Client{}
    response, err := client.Do(request)
    if (err != nil || response.StatusCode != http.StatusOK) {
        return response, errors.New(
            fmt.Sprintf("Cannot get content of booking website '%s'.",
                BOOKING_WEBSITE_URL))
    }

    return response, nil
}

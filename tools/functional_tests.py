"""
Functional API tests for the URL Shortener application.
"""

import requests
import time
import uuid
from config import BASE_URL, SHORTEN_ENDPOINT, REQUEST_TIMEOUT


class FunctionalTests:

    def __init__(self):
        self.results = []
        self.response_times = []

    def run_all(self):
        print("\nRunning Functional Tests...\n")

        self.test_application_health()
        self.test_create_short_url()
        self.test_duplicate_url()
        self.test_custom_alias()
        self.test_duplicate_alias()
        self.test_unknown_short_code()

        return self.results, self.response_times

    def record(self, test_name, passed, message):
        self.results.append({
            "test": test_name,
            "status": "PASS" if passed else "FAIL",
            "message": message
        })

        icon = "PASS" if passed else "FAIL"
        print(f"[{icon}] {test_name} -> {message}")

    ##############################################################

    def test_application_health(self):

        try:
            response = requests.get(
                BASE_URL + "/swagger-ui/index.html",
                timeout=REQUEST_TIMEOUT
            )

            self.record(
                "Application Health",
                response.status_code == 200,
                f"HTTP {response.status_code}"
            )

        except Exception as ex:

            self.record(
                "Application Health",
                False,
                str(ex)
            )

    ##############################################################

    def test_create_short_url(self):

        payload = {
            "url": "https://www.google.com"
        }

        start = time.perf_counter()

        response = requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload,
            timeout=REQUEST_TIMEOUT
        )

        end = time.perf_counter()

        self.response_times.append(end - start)

        passed = (
                response.status_code == 201 and
                "shortCode" in response.json()
        )

        self.record(
            "Create Short URL",
            passed,
            f"HTTP {response.status_code}"
        )

    ##############################################################

    def test_duplicate_url(self):

        payload = {
            "url": "https://www.google.com"
        }

        first = requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload
        )

        second = requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload
        )

        passed = (
                first.json()["shortCode"] ==
                second.json()["shortCode"]
        )

        self.record(
            "Duplicate URL",
            passed,
            "Existing short code returned"
        )

    ##############################################################

    def test_custom_alias(self):

        payload = {
            "url": f"https://example.com/{uuid.uuid4().hex}",
            "alias": f"alias-{uuid.uuid4().hex[:8]}"
        }   

        response = requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload
        )

        passed = (
            response.status_code == 201 and
            response.json()["shortCode"] == payload["alias"]
        )

        self.record(
            "Custom Alias",
            passed,
            f"HTTP {response.status_code}"
        )

##############################################################

    def test_duplicate_alias(self):

        payload = {
            "url": "https://example.com",
            "alias": "duplicate-demo"
        }

        requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload
        )

        response = requests.post(
            BASE_URL + SHORTEN_ENDPOINT,
            json=payload
        )

        self.record(
            "Duplicate Alias",
            response.status_code == 409,
            f"HTTP {response.status_code}"
        )

##############################################################

    def test_unknown_short_code(self):

        response = requests.get(
            BASE_URL + "/unknown123456"
        )

        self.record(
            "Unknown Short Code",
            response.status_code == 404,
            f"HTTP {response.status_code}"
        )
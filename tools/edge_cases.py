"""
Negative and edge case testing for the URL Shortener.
"""

import requests
import uuid

from config import BASE_URL, SHORTEN_ENDPOINT, REQUEST_TIMEOUT


class EdgeCaseTests:

    def __init__(self):

        self.results = []

    ############################################################

    def record(self, test_name, passed, message):

        self.results.append({
            "test": test_name,
            "status": "PASS" if passed else "FAIL",
            "message": message
        })

        icon = "PASS" if passed else "FAIL"

        print(f"[{icon}] {test_name} -> {message}")

    ############################################################

    def run_all(self):

        print("\nRunning Edge Case Tests...\n")

        self.test_empty_url()

        self.test_missing_url()

        self.test_invalid_url()

        self.test_unsupported_protocol()

        self.test_blank_alias()

        self.test_long_url()

        self.test_special_characters_alias()

        self.test_sql_injection()

        self.test_xss_attempt()

        return self.results

    ############################################################

    def test_empty_url(self):

        payload = {
            "url": ""
        }

        self.execute(
            "Empty URL",
            payload,
            400
        )

    ############################################################

    def test_missing_url(self):

        payload = {}

        self.execute(
            "Missing URL",
            payload,
            400
        )

    ############################################################

    def test_invalid_url(self):

        payload = {
            "url": "not-a-url"
        }

        self.execute(
            "Invalid URL",
            payload,
            400
        )

    ############################################################

    def test_unsupported_protocol(self):

        payload = {
            "url": "ftp://example.com"
        }

        self.execute(
            "Unsupported Protocol",
            payload,
            400
        )

    ############################################################

    def test_blank_alias(self):

        payload = {
            "url": f"https://example.com/{uuid.uuid4().hex}",
            "alias": f"alias-{uuid.uuid4().hex[:8]}"
        }

        self.execute(
            "Blank Alias",
            payload,
            201
        )

    ############################################################

    def test_long_url(self):

        very_long = (
                "https://example.com/" +
                "a" * 5000
        )

        payload = {
            "url": very_long
        }

        self.execute(
            "Very Long URL",
            payload,
            201
        )

    ############################################################

    def test_special_characters_alias(self):

        payload = {
            "url": "https://example.com",
            "alias": "@@@###"
        }

        self.execute(
            "Special Character Alias",
            payload,
            400
        )

    ############################################################

    def test_sql_injection(self):

        payload = {
            "url": "https://example.com",
            "alias": "'; DROP TABLE url_mapping;--"
        }

        self.execute(
            "SQL Injection Alias",
            payload,
            400
        )

    ############################################################

    def test_xss_attempt(self):

        payload = {
            "url": "https://example.com",
            "alias": "<script>alert(1)</script>"
        }

        self.execute(
            "XSS Alias",
            payload,
            400
        )

    ############################################################

    def execute(
            self,
            name,
            payload,
            expected_status
    ):

        try:

            response = requests.post(
                BASE_URL + SHORTEN_ENDPOINT,
                json=payload,
                timeout=REQUEST_TIMEOUT
            )

            self.record(
                name,
                response.status_code == expected_status,
                f"Expected {expected_status}, Actual {response.status_code}"
            )

        except Exception as ex:

            self.record(
                name,
                False,
                str(ex)
            )
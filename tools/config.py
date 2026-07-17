"""
Application configuration for the URL Shortener Test Harness.
Modify BASE_URL if the application is running on another host or port.
"""

BASE_URL = "http://localhost:8080"

SHORTEN_ENDPOINT = "/shorten"

REQUEST_TIMEOUT = 5

PERFORMANCE_TEST_ITERATIONS = 20

REPORT_DIRECTORY = "reports"

REPORT_FILE = "validation_report.md"
"""
URL Shortener Validation Harness

Entry point for executing:
- Functional Tests
- Edge Case Tests

Generates a Markdown validation report.
"""

import os
import sys
import time
import requests

from config import (
    BASE_URL,
    REQUEST_TIMEOUT
)

from functional_tests import FunctionalTests
from edge_cases import EdgeCaseTests
from report_generator import ReportGenerator


class TestHarness:

    ##############################################################

    def __init__(self):

        self.start_time = time.perf_counter()

    ##############################################################

    def print_banner(self):

        print("\n")
        print("=" * 60)
        print("      URL SHORTENER VALIDATION HARNESS")
        print("=" * 60)

    ##############################################################

    def health_check(self):

        print("\nChecking application availability...\n")

        try:

            response = requests.get(
                BASE_URL + "/swagger-ui/index.html",
                timeout=REQUEST_TIMEOUT
            )

            if response.status_code == 200:

                print("Application is running.")

                return True

            print(f"Unexpected HTTP Status : {response.status_code}")

            return False

        except Exception as ex:

            print("Application is NOT running.")

            print(ex)

            return False

    ##############################################################

    def execute(self):

        self.print_banner()

        if not self.health_check():

            print("\nStopping execution.")

            sys.exit(1)

        ##########################################################

        functional = FunctionalTests()

        functional_results, response_times = functional.run_all()

        ##########################################################

        edge = EdgeCaseTests()

        edge_results = edge.run_all()

        ##########################################################

        generator = ReportGenerator()

        report = generator.generate(

            functional_results,

            edge_results,

            response_times

        )

        ##########################################################

        end = time.perf_counter()

        print("\n")

        print("=" * 60)

        print("VALIDATION SUMMARY")

        print("=" * 60)

        total = len(functional_results) + len(edge_results)

        passed = len([
            r
            for r in (functional_results + edge_results)
            if r["status"] == "PASS"
        ])

        failed = total - passed

        print(f"Total Tests : {total}")

        print(f"Passed      : {passed}")

        print(f"Failed      : {failed}")

        print(f"Execution   : {end-self.start_time:.2f} sec")

        print()

        print(f"Report Generated : {os.path.abspath(report)}")

        print("=" * 60)

        ##########################################################

        if failed > 0:

            sys.exit(1)

        sys.exit(0)


##############################################################

if __name__ == "__main__":

    harness = TestHarness()

    harness.execute()
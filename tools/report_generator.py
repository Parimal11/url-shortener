"""
Generates a Markdown validation report for the URL Shortener application.
"""

import os
import statistics
from datetime import datetime

from config import REPORT_DIRECTORY, REPORT_FILE


class ReportGenerator:

    def __init__(self):

        self.timestamp = datetime.now()

    ###########################################################

    def generate(
            self,
            functional_results,
            edge_results,
            response_times
    ):

        os.makedirs(REPORT_DIRECTORY, exist_ok=True)

        report_path = os.path.join(
            REPORT_DIRECTORY,
            REPORT_FILE
        )

        all_results = functional_results + edge_results

        passed = len(
            [r for r in all_results if r["status"] == "PASS"]
        )

        failed = len(
            [r for r in all_results if r["status"] == "FAIL"]
        )

        total = len(all_results)

        with open(report_path, "w", encoding="utf-8") as file:

            file.write("# URL Shortener Validation Report\n\n")

            file.write(
                f"Generated : {self.timestamp}\n\n"
            )

            ##################################################

            file.write("## Summary\n\n")

            file.write(f"- Total Tests : {total}\n")
            file.write(f"- Passed : {passed}\n")
            file.write(f"- Failed : {failed}\n\n")

            ##################################################

            file.write("## Functional Tests\n\n")

            for result in functional_results:

                icon = "✅" if result["status"] == "PASS" else "❌"

                file.write(
                    f"{icon} {result['test']} - {result['message']}\n"
                )

            ##################################################

            file.write("\n## Edge Case Tests\n\n")

            for result in edge_results:

                icon = "✅" if result["status"] == "PASS" else "❌"

                file.write(
                    f"{icon} {result['test']} - {result['message']}\n"
                )

            ##################################################

            file.write("\n## Performance\n\n")

            if response_times:

                avg = statistics.mean(response_times)

                minimum = min(response_times)

                maximum = max(response_times)

                file.write(
                    f"- Average : {avg*1000:.2f} ms\n"
                )

                file.write(
                    f"- Minimum : {minimum*1000:.2f} ms\n"
                )

                file.write(
                    f"- Maximum : {maximum*1000:.2f} ms\n"
                )

            else:

                file.write("No performance data collected.\n")

            ##################################################

            file.write("\n## Failure Analysis\n\n")

            failures = [
                r for r in all_results
                if r["status"] == "FAIL"
            ]

            if not failures:

                file.write(
                    "No failures detected.\n"
                )

            else:

                for failure in failures:

                    file.write(
                        f"- {failure['test']} : "
                        f"{failure['message']}\n"
                    )

            ##################################################

            file.write("\n## Engineering Recommendations\n\n")

            recommendations = self.get_recommendations(
                failures,
                response_times
            )

            for recommendation in recommendations:

                file.write(
                    f"- {recommendation}\n"
                )

        return report_path

    ###########################################################

    def get_recommendations(
            self,
            failures,
            response_times
    ):

        recommendations = []

        if not failures:

            recommendations.append(
                "Application passed all validation tests."
            )

        if response_times:

            average = statistics.mean(response_times)

            if average > 0.10:

                recommendations.append(
                    "Average response time exceeds 100 ms. "
                    "Consider introducing Redis caching."
                )

        recommendations.extend([

            "Consider implementing URL expiration.",

            "Add click analytics.",

            "Introduce rate limiting for abuse prevention.",

            "Deploy behind a reverse proxy for production.",

            "Expose Prometheus metrics for monitoring."

        ])

        return recommendations
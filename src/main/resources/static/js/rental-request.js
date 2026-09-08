document.addEventListener("DOMContentLoaded", function () {

    const startInput =
        document.getElementById("startDateTime");

    const endInput =
        document.getElementById("endDateTime");


    if (!startInput || !endInput) {
        return;
    }


    // =====================================================
    // FORMAT DATE FOR datetime-local INPUT
    // =====================================================

    function formatDateTime(date) {

        const year =
            date.getFullYear();

        const month =
            String(date.getMonth() + 1)
                .padStart(2, "0");

        const day =
            String(date.getDate())
                .padStart(2, "0");

        const hour =
            String(date.getHours())
                .padStart(2, "0");

        const minute =
            String(date.getMinutes())
                .padStart(2, "0");


        return (
            year +
            "-" +
            month +
            "-" +
            day +
            "T" +
            hour +
            ":" +
            minute
        );
    }


    // =====================================================
    // PREVENT PAST BOOKINGS
    // =====================================================

    function updateMinimumTime() {

        const now =
            new Date();

        now.setSeconds(0);
        now.setMilliseconds(0);

        startInput.min =
            formatDateTime(now);
    }


    updateMinimumTime();


    // =====================================================
    // AUTOMATIC 1-HOUR END TIME
    // =====================================================

    startInput.addEventListener(
        "change",
        function () {

            if (!startInput.value) {

                endInput.value = "";
                return;
            }


            const selectedStart =
                new Date(startInput.value);

            const now =
                new Date();


            // Prevent past date/time
            if (selectedStart < now) {

                alert(
                    "Please select a future date and time."
                );

                startInput.value = "";
                endInput.value = "";

                updateMinimumTime();

                return;
            }


            // Add exactly 1 hour
            const endTime =
                new Date(
                    selectedStart.getTime()
                    + (60 * 60 * 1000)
                );


            endInput.value =
                formatDateTime(endTime);

        }
    );


    // =====================================================
    // UPDATE MINIMUM TIME IF PAGE STAYS OPEN
    // =====================================================

    setInterval(
        updateMinimumTime,
        60000
    );

});
document.addEventListener("DOMContentLoaded", function () {

    const calendar =
        document.getElementById("availabilityCalendar");

    const calendarTitle =
        document.getElementById("calendarTitle");

    const previousButton =
        document.getElementById("previousMonth");

    const nextButton =
        document.getElementById("nextMonth");

    const todayButton =
        document.getElementById("todayButton");


    if (!calendar) {
        return;
    }


    let bookings = [];

    const today = new Date();

    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth();


    // =====================================================
    // LOAD BOOKINGS / BLOCK-OUTS FROM SPRING BOOT
    // =====================================================

    async function loadBookings() {

        try {

            const response =
                await fetch(
                    "/member/availability/events",
                    {
                        cache: "no-store"
                    }
                );


            if (!response.ok) {

                console.error(
                    "Unable to load booking information."
                );

                return;
            }


            bookings =
                await response.json();


            renderCalendar();

        } catch (error) {

            console.error(
                "Calendar loading error:",
                error
            );

        }
    }


    // =====================================================
    // FORMAT TIME
    // =====================================================

    function formatTime(dateTimeString) {

        if (!dateTimeString) {
            return "";
        }


        const timePart =
            dateTimeString.substring(11, 16);

        const parts =
            timePart.split(":");

        let hour =
            parseInt(parts[0], 10);

        const minute =
            parts[1];

        const period =
            hour >= 12 ? "PM" : "AM";


        hour = hour % 12;

        if (hour === 0) {
            hour = 12;
        }


        return (
            hour
            + ":"
            + minute
            + " "
            + period
        );
    }


    // =====================================================
    // CREATE DATE KEY
    // =====================================================

    function getDateKey(
        year,
        month,
        day
    ) {

        const monthString =
            String(month + 1)
                .padStart(2, "0");

        const dayString =
            String(day)
                .padStart(2, "0");


        return (
            year
            + "-"
            + monthString
            + "-"
            + dayString
        );
    }


    // =====================================================
    // RENDER CALENDAR
    // =====================================================

    function renderCalendar() {

        calendar.innerHTML = "";


        const monthNames = [
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        ];


        calendarTitle.textContent =
            monthNames[currentMonth]
            + " "
            + currentYear;


        const firstDay =
            new Date(
                currentYear,
                currentMonth,
                1
            ).getDay();


        const daysInMonth =
            new Date(
                currentYear,
                currentMonth + 1,
                0
            ).getDate();


        // =================================================
        // EMPTY CELLS BEFORE FIRST DAY
        // =================================================

        for (
            let i = 0;
            i < firstDay;
            i++
        ) {

            const emptyCell =
                document.createElement("div");


            emptyCell.classList.add(
                "calendar-day",
                "calendar-empty"
            );


            calendar.appendChild(
                emptyCell
            );
        }


        // =================================================
        // CALENDAR DAYS
        // =================================================

        for (
            let day = 1;
            day <= daysInMonth;
            day++
        ) {

            const dayCell =
                document.createElement("div");


            dayCell.classList.add(
                "calendar-day"
            );


            const dateKey =
                getDateKey(
                    currentYear,
                    currentMonth,
                    day
                );


            const number =
                document.createElement("div");


            number.classList.add(
                "calendar-day-number"
            );


            number.textContent =
                String(day);


            dayCell.appendChild(
                number
            );


            // =============================================
            // TODAY
            // =============================================

            if (
                currentYear === today.getFullYear()
                &&
                currentMonth === today.getMonth()
                &&
                day === today.getDate()
            ) {

                dayCell.classList.add(
                    "calendar-today"
                );
            }


            // =============================================
            // EVENTS FOR THIS DATE
            // =============================================

            const dayBookings =
                bookings.filter(
                    function (booking) {

                        if (!booking.start) {
                            return false;
                        }


                        return (
                            booking.start.substring(
                                0,
                                10
                            )
                            === dateKey
                        );
                    }
                );


            // =============================================
            // DISPLAY BOOKINGS / BLOCK-OUTS
            // =============================================

            dayBookings.forEach(
                function (booking) {


                    const bookingElement =
                        document.createElement(
                            "div"
                        );


                    bookingElement.classList.add(
                        "calendar-booking"
                    );


                    // =====================================
                    // BLOCK-OUT
                    // =====================================

                    if (
                        booking.eventType
                        === "BLOCKOUT"
                    ) {

                        bookingElement.classList.add(
                            "calendar-blockout"
                        );
                    }


                    // =====================================
                    // FACILITY NAME
                    // =====================================

                    const facilityName =
                        document.createElement(
                            "strong"
                        );


                    facilityName.textContent =
                        booking.facility;


                    // =====================================
                    // EVENT TYPE
                    // =====================================

                    const eventType =
                        document.createElement(
                            "span"
                        );


                    eventType.classList.add(
                        "calendar-event-type"
                    );


                    if (
                        booking.eventType
                        === "BLOCKOUT"
                    ) {

                        eventType.textContent =
                            "Unavailable";

                    } else {

                        eventType.textContent =
                            "Booked";
                    }


                    // =====================================
                    // TIME
                    // =====================================

                    const bookingTime =
                        document.createElement(
                            "span"
                        );


                    bookingTime.textContent =
                        formatTime(
                            booking.start
                        )
                        + " - "
                        + formatTime(
                            booking.end
                        );


                    // =====================================
                    // ADD EVENT
                    // =====================================

                    bookingElement.appendChild(
                        facilityName
                    );


                    bookingElement.appendChild(
                        eventType
                    );


                    bookingElement.appendChild(
                        bookingTime
                    );


                    dayCell.appendChild(
                        bookingElement
                    );

                }
            );


            // ADD DAY TO CALENDAR
            calendar.appendChild(
                dayCell
            );

        }

    }


    // =====================================================
    // PREVIOUS MONTH
    // =====================================================

    previousButton.addEventListener(
        "click",
        function () {

            currentMonth--;


            if (currentMonth < 0) {

                currentMonth = 11;
                currentYear--;

            }


            renderCalendar();

        }
    );


    // =====================================================
    // NEXT MONTH
    // =====================================================

    nextButton.addEventListener(
        "click",
        function () {

            currentMonth++;


            if (currentMonth > 11) {

                currentMonth = 0;
                currentYear++;

            }


            renderCalendar();

        }
    );


    // =====================================================
    // TODAY
    // =====================================================

    todayButton.addEventListener(
        "click",
        function () {

            currentYear =
                today.getFullYear();


            currentMonth =
                today.getMonth();


            renderCalendar();

        }
    );


    // =====================================================
    // FIRST DATABASE LOAD
    // =====================================================

    void loadBookings();


    // =====================================================
    // AUTO REFRESH EVERY 2 SECONDS
    // =====================================================

    setInterval(
        function () {

            void loadBookings();

        },
        2000
    );

});
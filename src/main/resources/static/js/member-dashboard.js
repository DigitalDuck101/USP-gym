document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // ACTIVE NAVBAR LINK
    // =====================================================

    const currentPath =
        window.location.pathname;

    const navLinks =
        document.querySelectorAll(
            "#memberNavbar .nav-link"
        );


    navLinks.forEach(function (link) {

        const href =
            link.getAttribute("href");

        link.classList.remove("active");


        if (
            currentPath === "/member/dashboard" &&
            href === "/member/dashboard"
        ) {

            link.classList.add("active");

        }

        else if (
            currentPath.startsWith("/profile") &&
            href === "/profile"
        ) {

            link.classList.add("active");

        }

        else if (
            currentPath.startsWith("/member/classes") &&
            href === "/member/classes"
        ) {

            link.classList.add("active");

        }

        else if (
            currentPath.startsWith("/member/bookings") &&
            href === "/member/bookings"
        ) {

            link.classList.add("active");

        }

        else if (
            currentPath.startsWith("/renter/rentals") &&
            href === "/renter/rentals"
        ) {

            link.classList.add("active");

        }

    });


    // =====================================================
    // CLOSE MOBILE MENU AFTER CLICK
    // =====================================================

    const navbarCollapse =
        document.getElementById("memberNavbar");


    navLinks.forEach(function (link) {

        link.addEventListener(
            "click",
            function () {

                if (
                    navbarCollapse &&
                    navbarCollapse.classList.contains("show") &&
                    typeof bootstrap !== "undefined"
                ) {

                    const collapse =
                        bootstrap.Collapse.getOrCreateInstance(
                            navbarCollapse
                        );

                    collapse.hide();

                }

            }
        );

    });

});
document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // ACTIVE ADMIN NAVBAR LINK
    // =====================================================

    const currentPath = window.location.pathname;

    const navLinks = document.querySelectorAll(
        "nav a[href], .navbar a[href]"
    );

    navLinks.forEach(function (link) {

        const linkPath = link.getAttribute("href");

        if (!linkPath) {
            return;
        }

        // Remove old active state
        link.classList.remove("active");


        // Dashboard
        if (
            currentPath === "/admin/dashboard" &&
            linkPath === "/admin/dashboard"
        ) {
            link.classList.add("active");
        }


        // Members
        else if (
            currentPath.startsWith("/members") &&
            linkPath === "/members"
        ) {
            link.classList.add("active");
        }


        // Trainers
        else if (
            currentPath.startsWith("/trainers") &&
            linkPath === "/trainers"
        ) {
            link.classList.add("active");
        }


        // Fitness Classes
        else if (
            currentPath.startsWith("/fitness-classes") &&
            linkPath === "/fitness-classes"
        ) {
            link.classList.add("active");
        }


        // Student Approvals
        else if (
            currentPath.startsWith("/admin/student-approvals") &&
            linkPath === "/admin/student-approvals"
        ) {
            link.classList.add("active");
        }


        // Sports Facilities
        else if (
            currentPath.startsWith("/admin/facilities") &&
            linkPath === "/admin/facilities"
        ) {
            link.classList.add("active");
        }


        // Facility Rentals
        else if (
            currentPath.startsWith("/admin/rentals") &&
            linkPath === "/admin/rentals"
        ) {
            link.classList.add("active");
        }

    });


    // =====================================================
    // MOBILE NAVBAR - CLOSE AFTER CLICKING LINK
    // =====================================================

    const navbarCollapse =
        document.querySelector(".navbar-collapse");

    const mobileNavLinks =
        document.querySelectorAll(".navbar-collapse .nav-link");


    mobileNavLinks.forEach(function (link) {

        link.addEventListener("click", function () {

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

        });

    });


    // =====================================================
    // DASHBOARD CARD CLICK EFFECT
    // =====================================================

    const dashboardCards =
        document.querySelectorAll(".dashboard-card");

    dashboardCards.forEach(function (card) {

        card.addEventListener("mousedown", function () {
            card.style.transform = "scale(0.99)";
        });

        card.addEventListener("mouseup", function () {
            card.style.transform = "";
        });

        card.addEventListener("mouseleave", function () {
            card.style.transform = "";
        });

    });

});
document.addEventListener("DOMContentLoaded", function () {

    // Automatically display current year in footer
    const yearElement =
        document.getElementById("currentYear");

    if (yearElement) {
        yearElement.textContent =
            new Date().getFullYear();
    }


    // Close mobile navbar when a navigation item is selected
    const navbar =
        document.getElementById("mainNavbar");

    const navLinks =
        document.querySelectorAll("#mainNavbar .nav-link");


    navLinks.forEach(function (link) {

        link.addEventListener("click", function () {

            if (
                window.innerWidth < 992 &&
                navbar.classList.contains("show")
            ) {

                const bootstrapCollapse =
                    bootstrap.Collapse.getOrCreateInstance(navbar);

                bootstrapCollapse.hide();

            }

        });

    });

});
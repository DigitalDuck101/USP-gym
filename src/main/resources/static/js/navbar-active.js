document.addEventListener("DOMContentLoaded", function () {

    const currentPath = window.location.pathname;

    const navLinks = document.querySelectorAll(".navbar .nav-link");

    navLinks.forEach(function (link) {

        const href = link.getAttribute("href");

        if (!href || href === "#") {
            return;
        }

        link.classList.remove("active-page");

        // Exact page match
        if (currentPath === href) {
            link.classList.add("active-page");
            return;
        }

        // Child page match
        if (href !== "/" && currentPath.startsWith(href + "/")) {
            link.classList.add("active-page");
        }

    });

});
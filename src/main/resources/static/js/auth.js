document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // CURRENT YEAR
    // =====================================================

    const yearElement =
        document.getElementById("currentYear");

    if (yearElement) {
        yearElement.textContent =
            new Date().getFullYear();
    }


    // =====================================================
    // PASSWORD SHOW / HIDE
    // =====================================================

    const passwordInput =
        document.getElementById("password");

    const togglePassword =
        document.getElementById("togglePassword");


    if (passwordInput && togglePassword) {

        togglePassword.addEventListener(
            "click",
            function () {

                if (passwordInput.type === "password") {

                    passwordInput.type = "text";
                    togglePassword.textContent = "Hide";

                } else {

                    passwordInput.type = "password";
                    togglePassword.textContent = "Show";

                }

            }
        );
    }


    // =====================================================
    // CONFIRM PASSWORD SHOW / HIDE
    // =====================================================

    const confirmPassword =
        document.getElementById("confirmPassword");

    const toggleConfirmPassword =
        document.getElementById("toggleConfirmPassword");


    if (confirmPassword && toggleConfirmPassword) {

        toggleConfirmPassword.addEventListener(
            "click",
            function () {

                if (confirmPassword.type === "password") {

                    confirmPassword.type = "text";
                    toggleConfirmPassword.textContent = "Hide";

                } else {

                    confirmPassword.type = "password";
                    toggleConfirmPassword.textContent = "Show";

                }

            }
        );
    }


    // =====================================================
    // REGISTRATION ELEMENTS
    // =====================================================

    const memberType =
        document.getElementById("memberType");

    const studentIdSection =
        document.getElementById("studentIdSection");

    const studentId =
        document.getElementById("studentId");

    const email =
        document.getElementById("email");

    const username =
        document.getElementById("username");

    const registrationForm =
        document.getElementById("registrationForm");

    const clientError =
        document.getElementById("clientError");


    // =====================================================
    // SHOW / HIDE STUDENT ID
    // =====================================================

    function updateAccountType() {

        if (
            !memberType ||
            !studentIdSection ||
            !studentId
        ) {
            return;
        }


        // =========================================
        // USP STUDENT
        // =========================================

        if (memberType.value === "STUDENT") {

            studentIdSection.style.display = "block";

            studentId.required = true;


            // Student ID becomes login username
            if (username) {

                username.value =
                    studentId.value.trim();

            }

        }


            // =========================================
            // COMMUNITY MEMBER
        // =========================================

        else if (memberType.value === "RENTER") {

            studentIdSection.style.display = "none";

            studentId.required = false;

            studentId.value = "";


            // Email becomes login username
            if (username && email) {

                username.value =
                    email.value.trim();

            }

        }


            // =========================================
            // NOTHING SELECTED
        // =========================================

        else {

            studentIdSection.style.display = "none";

            studentId.required = false;

            studentId.value = "";

            if (username) {
                username.value = "";
            }

        }
    }


    // =====================================================
    // ACCOUNT TYPE CHANGE
    // =====================================================

    if (memberType) {

        memberType.addEventListener(
            "change",
            updateAccountType
        );

        updateAccountType();
    }


    // =====================================================
    // STUDENT ID → USERNAME
    // =====================================================

    if (studentId && memberType && username) {

        studentId.addEventListener(
            "input",
            function () {

                if (
                    memberType.value === "STUDENT"
                ) {

                    username.value =
                        studentId.value.trim();

                }

            }
        );
    }


    // =====================================================
    // COMMUNITY EMAIL → USERNAME
    // =====================================================

    if (email && memberType && username) {

        email.addEventListener(
            "input",
            function () {

                if (
                    memberType.value === "RENTER"
                ) {

                    username.value =
                        email.value.trim();

                }

            }
        );
    }


    // =====================================================
    // REGISTRATION FORM VALIDATION
    // =====================================================

    if (registrationForm) {

        registrationForm.addEventListener(
            "submit",
            function (event) {


                // =========================================
                // STUDENT LOGIN IDENTIFIER
                // =========================================

                if (
                    memberType &&
                    memberType.value === "STUDENT"
                ) {

                    if (
                        !studentId ||
                        !studentId.value.trim()
                    ) {

                        event.preventDefault();

                        if (clientError) {

                            clientError.textContent =
                                "Student ID is required for USP students.";

                            clientError.classList.remove(
                                "d-none"
                            );
                        }

                        if (studentId) {
                            studentId.focus();
                        }

                        return;
                    }


                    if (username) {

                        username.value =
                            studentId.value.trim();

                    }

                }


                    // =========================================
                    // COMMUNITY MEMBER LOGIN IDENTIFIER
                // =========================================

                else if (
                    memberType &&
                    memberType.value === "RENTER"
                ) {

                    if (
                        !email ||
                        !email.value.trim()
                    ) {

                        event.preventDefault();

                        if (clientError) {

                            clientError.textContent =
                                "Email address is required.";

                            clientError.classList.remove(
                                "d-none"
                            );
                        }

                        if (email) {
                            email.focus();
                        }

                        return;
                    }


                    if (username) {

                        username.value =
                            email.value.trim();

                    }

                }


                // =========================================
                // PASSWORD LENGTH
                // =========================================

                if (
                    passwordInput &&
                    passwordInput.value.length < 8
                ) {

                    event.preventDefault();

                    if (clientError) {

                        clientError.textContent =
                            "Password must contain at least 8 characters.";

                        clientError.classList.remove(
                            "d-none"
                        );
                    }

                    passwordInput.focus();

                    return;
                }


                // =========================================
                // PASSWORD MATCH
                // =========================================

                if (
                    passwordInput &&
                    confirmPassword &&
                    passwordInput.value !==
                    confirmPassword.value
                ) {

                    event.preventDefault();

                    if (clientError) {

                        clientError.textContent =
                            "Passwords do not match.";

                        clientError.classList.remove(
                            "d-none"
                        );
                    }

                    confirmPassword.focus();

                    return;
                }


                if (clientError) {

                    clientError.classList.add(
                        "d-none"
                    );

                }

            }
        );
    }

});
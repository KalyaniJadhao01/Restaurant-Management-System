// ==========================================
// SETTINGS CONFIGURATION
// ==========================================

const token = localStorage.getItem("token");


if (!token) {

    window.location.href = "login.html";

}



// ==========================================
// LOAD USER INFO
// ==========================================

document.addEventListener(
    "DOMContentLoaded",
    () => {

        loadUserInfo();

        loadSavedSettings();

    });




// ==========================================
// USER INFO FROM TOKEN
// ==========================================

function loadUserInfo() {

    const role =
        localStorage.getItem("role");


    if (role) {

        document.getElementById("userRole")
            .innerText =
            role;

    }


}





// ==========================================
// CHANGE PASSWORD
// ==========================================

function changePassword() {


    const oldPassword =
        document.getElementById("oldPassword").value;


    const newPassword =
        document.getElementById("newPassword").value;


    const confirmPassword =
        document.getElementById("confirmPassword").value;



    if (!oldPassword ||
        !newPassword ||
        !confirmPassword) {

        alert("Please fill all password fields");

        return;

    }



    if (newPassword !== confirmPassword) {

        alert("New password and confirm password do not match");

        return;

    }


    /*
     Backend API is not created yet.
    
     When you add it:
    
     PUT /api/users/change-password
    
     send:
    
     {
        oldPassword:"",
        newPassword:""
     }
    
    */


    alert(
        "Password update API is not connected yet."
    );



}




// ==========================================
// SAVE RESTAURANT SETTINGS
// ==========================================

function saveSettings() {


    const settings = {


        restaurantName:
            document.getElementById("restaurantName").value,


        restaurantAddress:
            document.getElementById("restaurantAddress").value,


        theme:
            document.getElementById("theme").value


    };



    localStorage.setItem(

        "restaurantSettings",

        JSON.stringify(settings)

    );



    applyTheme(
        settings.theme
    );



    alert(
        "Settings saved successfully"
    );



}




// ==========================================
// LOAD SAVED SETTINGS
// ==========================================

function loadSavedSettings() {


    const saved =
        localStorage.getItem(
            "restaurantSettings"
        );



    if (!saved) {

        return;

    }



    const settings =
        JSON.parse(saved);



    document.getElementById("restaurantName")
        .value =
        settings.restaurantName || "RestaurantMS";



    document.getElementById("restaurantAddress")
        .value =
        settings.restaurantAddress || "";



    document.getElementById("theme")
        .value =
        settings.theme || "light";



}




// ==========================================
// THEME
// ==========================================

document
    .getElementById("theme")
    .addEventListener(
        "change",
        function () {

            applyTheme(this.value);

        });




function applyTheme(theme) {


    if (theme === "dark") {


        document.body.style.background =
            "#111827";


        document.body.style.color =
            "#fff";


    }

    else {


        document.body.style.background =
            "#f5f7fb";


        document.body.style.color =
            "#333";


    }



}




// ==========================================
// LOGOUT
// ==========================================

function logout() {


    localStorage.removeItem("token");

    localStorage.removeItem("role");


    window.location.href = "login.html";


}
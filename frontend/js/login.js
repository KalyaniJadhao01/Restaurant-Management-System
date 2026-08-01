// ===============================
// Backend Login API
// ===============================

const LOGIN_URL = `${API_BASE_URL}/auth/login`;


// ===============================
// DOM Elements
// ===============================

const loginForm = document.getElementById("loginForm");

const emailInput = document.getElementById("email");

const passwordInput = document.getElementById("password");

const togglePassword = document.getElementById("togglePassword");

const message = document.getElementById("message");

const loginBtn = document.getElementById("loginBtn");



// ===============================
// Show / Hide Password
// ===============================

togglePassword.addEventListener("click",()=>{


    if(passwordInput.type === "password"){


        passwordInput.type = "text";

        togglePassword.classList.remove("fa-eye");

        togglePassword.classList.add("fa-eye-slash");


    }

    else{


        passwordInput.type = "password";

        togglePassword.classList.remove("fa-eye-slash");

        togglePassword.classList.add("fa-eye");


    }


});




// ===============================
// Login Submit
// ===============================

loginForm.addEventListener("submit",(event)=>{


    event.preventDefault();


    const loginData = {


        email: emailInput.value.trim(),


        password: passwordInput.value.trim()


    };



    if(!loginData.email || !loginData.password){


        showMessage(
            "Please enter email and password",
            "red"
        );

        return;

    }



    loginBtn.disabled = true;

    loginBtn.innerHTML = "Logging in...";




    axios.post(
        LOGIN_URL,
        loginData
    )


    .then(response=>{


        console.log(response.data);



        /*
          Your backend may return:

          {
             token:"jwt-token"
          }

          or

          {
             accessToken:"jwt-token"
          }

        */


        const token =
            response.data.token ??
            response.data.accessToken;



        if(!token){


            showMessage(
                "Token not received from server",
                "red"
            );


            return;

        }




        // Store JWT

        localStorage.setItem(
            "token",
            token
        );



        showMessage(
            "Login successful...",
            "green"
        );



        setTimeout(()=>{


            window.location.href="dashboard.html";


        },800);



    })



    .catch(error=>{


        console.error(error);



        if(error.response){


            if(error.response.status === 401){


                showMessage(
                    "Invalid email or password",
                    "red"
                );


            }

            else{


                showMessage(
                    "Login failed",
                    "red"
                );


            }


        }


        else{


            showMessage(
                "Server not reachable",
                "red"
            );


        }



    })



    .finally(()=>{


        loginBtn.disabled=false;

        loginBtn.innerHTML="Login";


    });



});




// ===============================
// Message Helper
// ===============================

function showMessage(text,color){


    message.innerText=text;

    message.style.color=color;


}
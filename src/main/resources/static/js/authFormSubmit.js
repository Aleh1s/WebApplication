const form = document.querySelector(".form");
const authUrl = 'http://localhost:8080/api/auth/login';
let token = '';

const getBody = () => {
    let email = document.querySelector(".email");
    let password = document.querySelector(".password");

    let body = {
        email: email.value,
        password: password.value
    };

    return JSON.stringify(body);
}

function auth (e) {
    e.preventDefault()

    const headers = {
        'Content-Type': 'application/json'
    }

    return fetch(authUrl, {
        method: 'POST',
        body: getBody(),
        headers: headers
    })
        .then(response => response.json().then(data => {
            localStorage.setItem('token', 'Bearer_' + data['token'])
            localStorage.setItem('refreshToken','Bearer_' + data['refreshToken'])
        }))
        .catch(err => console.log(err));
}

form.addEventListener('submit', auth);

const refreshTokenForm = document.querySelector(".refresh_token")
const refreshTokenUrl = "http://localhost:8080/api/auth/token/refresh"

const refreshTokenRequest = (e) => {
    e.preventDefault()

    const refreshToken = localStorage.getItem("refreshToken")

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': refreshToken
    }

    return fetch(refreshTokenUrl, {
        method: 'GET',
        headers: headers
    })
        .then(response => response.json().then(data => {
            localStorage.setItem('token', 'Bearer_' + data['token'])
            localStorage.setItem('refreshToken', 'Bearer_' + data['refreshToken'])
        }))
        .catch(err => console.log(err))
}

refreshTokenForm.addEventListener('submit', refreshTokenRequest)

const getUsersForm = document.querySelector('.getUsers')
let getUsersUrl = `http://localhost:8080/api/`;

const getUsers = (e) => {
    e.preventDefault()

    const userId = document.querySelector('.userId').value
    getUsersUrl = getUsersUrl + userId

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem('token')
    }

    return fetch(getUsersUrl, {
        method: 'GET',
        headers: headers
    })
        .then(response => console.log(response))
        .catch(err => console.log(err))
}

getUsersForm.addEventListener('submit', getUsers)

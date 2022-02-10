const form = document.querySelector('.form')
const registerUrl = 'http://localhost:8080/api/registration/register'

const getBody = () => {
    let name = document.querySelector('.name')
    let email = document.querySelector('.email')
    let password = document.querySelector('.password')

    const values = {
        name: name.value,
        email: email.value,
        password: password.value
    }

    return JSON.stringify(values)
}

function sendRequest (method, url, body = null) {

    const headers = {
        'Content-Type': 'application/json'
    }

    return fetch(url, {
        method: method,
        body: body,
        headers: headers
    })
        .then(response => response.json())

}

function register (e) {
    e.preventDefault()

    const body = getBody()

    sendRequest('POST', registerUrl, body)
        .then(data => console.log(data))
        .catch(err => console.log(err))

}

form.addEventListener('submit', register)


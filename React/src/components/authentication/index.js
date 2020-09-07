import React, { Component } from "react"
import PropTypes from 'prop-types'


const AuthContext = React.createContext()

export class AuthenticatedApp extends Component {

    constructor(){
        super()


        this.state = {
            authenticated: false,
            user : {},
            error: {}
        }
    }

    login = (user, pass) => {
        fetch("http://localhost:8081/login", {method:'POST', body:JSON.stringify({username:user, password:pass})})
            .then(response => {
                const codigo = response.status;

                if(codigo === 200){
                    const token = response.headers.get("Authorization");
                    const decodeToken = JSON.parse(atob(token.split('.')[1]));
                    this.setState(prev => ({...prev, authenticated: true, user: {username:user, rol: decodeToken.rol}, token:token}),
                        () => {})
                }
                else
                    this.setState(prev => ({...prev, error: { code: "INVALID_LOGIN", message: "Usuario o contraseña no válidos" }}))
            })
    }

    logout = () => {
        this.setState(prev => ({...prev, authenticated: false, user: {}}), () => {localStorage.removeItem("user")})
    }

    render(){
        const auth = {
            authenticated: this.state.authenticated,
            user: this.state.user,
            token: this.state.token,
            error: this.state.error,
            login: this.login,
            logout: this.logout
        }

        console.log(this.state.token)

        return <AuthContext.Provider value = { auth } >
            { this.props.children }
        </AuthContext.Provider>
    }
}

export const Authentication = AuthContext.Consumer
export class AuthenticatedOnly extends Component {
    static propTypes = {
        requiredRole: PropTypes.string,
        children: PropTypes.element.isRequired
    }

    static defaultProps = {
        requiredRole : null
    }

    render(){
        return <Authentication>
            {
                auth => {
                    if (auth.authenticated && (this.props.requiredRole === null || this.props.requiredRole.includes(auth.user.rol)))
                        return this.props.children
                }
            }
        </Authentication>
    }
}
export class UnauthenticatedOnly extends Component {
    static propTypes = {
        children: PropTypes.element.isRequired
    }

    render(){
        return <Authentication>
            {
                auth => {
                    if (!auth.authenticated)
                        return this.props.children
                }
            }
        </Authentication>
    }
}
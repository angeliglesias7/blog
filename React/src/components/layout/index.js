import React, { Component } from 'react'
import NavigationBar from "../navbar"
import { Container } from "reactstrap"
import {PublicacionList, PublicacionCompleta} from "../publicaciones"
import Login from "../login"
import Logout from "../logout"
import Register from "../register"
import { Switch, Route, Redirect } from "react-router-dom"


export default class Layout extends Component {
    render(){
        return <>
            <NavigationBar/>
            <Container className = "pt-3" tag = "main">
                <Switch>
                    <Route path = "/posts/:id" component = { PublicacionCompleta }/>
                    <Route path = "/posts" component = { PublicacionList } />
                    <Route path = "/login" component = { Login } />
                    <Route path = "/logout" component = { Logout } />
                    <Route path = "/register" component = { Register } />
                    <Redirect from = "/" to = "/posts"/>
                </Switch>
            </Container>
        </>
    }
}
import React, { Component } from "react"

import {
    Alert,
    Button,
    Card,
    CardHeader,
    CardBody,
    CardFooter,
    CardTitle,
    Form,
    FormGroup,
    Label,
    Input
} from 'reactstrap'

import {Authentication} from "../authentication";

export default class Register extends Component {
    render(){
        return <Authentication>
            {
                auth => <RegisterDialog login={auth.login}/>
            }
        </Authentication>
    }
}

export class RegisterDialog extends Component {
    constructor(props){
        super(props)

        this.state = {
            alert: {
                status: ""
            },
            username : "",
            password : "",
            email : "",

        }
    }

    onUsernameChange = event => {
        let value = event.target !== null ? event.target.value : ""
        this.setState(prev => ({...prev, username: value}))
    }

    onPasswordChange = event => {
        let value = event.target !== null ? event.target.value : ""
        this.setState(prev => ({...prev, password: value}))
    }

    onEmailChange = event => {
        let value = event.target !== null ? event.target.value : ""
        this.setState(prev => ({...prev, email: value}))
    }

    onRegisterButtonClick = () => {
        this.doRegister(this.state.username, this.state.password, this.state.email)
    }

    doRegister = (user, pass, mail) => {
        fetch("http://localhost:8081/usuarios", {method:'POST',
            headers:{
            'Accept': 'application/json;charset=UTF-8', 'Content-Type': 'application/json;charset=UTF-8'
            },
            body:JSON.stringify({username:user, password:pass, email:mail})})
            .then(response => {

                const respuesta = response.status;

                let error = 0

                if(respuesta === 201){
                    this.setState(prev => ({...prev, alert: {status: "OK", message: "El usuario se ha registrado con éxito"}}))
                    this.props.login(this.state.username, this.state.password)

                }
                else if(respuesta === 409) {
                    this.setState(prev => ({...prev, alert: {status: "Error", message: "Error. El usuario ya ha sido creado"}}))
                    error = 1
                }

                else if(respuesta !== 201 && respuesta !== 409 && error === 0){
                    this.setState(prev => ({...prev, alert: {status: "Error", message: "Error. El usuario ya ha sido creado"}}))
                }

            })

    }

    render() {
        return <>
            <Card color="blue">
                <CardHeader>
                    <CardTitle className={"colorUsuario"}>Register</CardTitle>
                </CardHeader>
                <CardBody>
                    <Form>
                        <FormGroup>
                            <Label className={"colorTitulo"}>Usuario: </Label>
                            <Input value={this.state.username} onChange={this.onUsernameChange} required/>
                        </FormGroup>
                        <FormGroup>
                            <Label className={"colorTitulo"}>Contraseña: </Label>
                            <Input type="password" value={this.state.password} onChange={this.onPasswordChange} required/>
                        </FormGroup>
                        <FormGroup>
                            <Label className={"colorTitulo"}>Email: </Label>
                            <Input value={this.state.email} onChange={this.onEmailChange}/>
                        </FormGroup>
                    </Form>
                </CardBody>
                <CardFooter>
                    <Button block onClick={this.onRegisterButtonClick}>Register</Button>
                </CardFooter>
            </Card>
            <Alert
                color={this.state.alert.status === "OK" ? "success" : "danger"}
                isOpen = {this.state.alert.status !== ""}
                toggle = { () => this.setState(prev => ({...prev, alert: {status: ""}})) }
            >
                {this.state.alert.message}
            </Alert>
        </>
    }
}




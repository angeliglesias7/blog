import React, { PureComponent as Component } from 'react'
import {FiSearch as SearchIcon} from "react-icons/fi"
import {
    Navbar,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Form,
    FormGroup,
    Input,
    InputGroup,
    InputGroupAddon,
    InputGroupText,
    Button
} from 'reactstrap'
import './styles.css';
import { Link } from 'react-router-dom'
import {AuthenticatedOnly, Authentication, UnauthenticatedOnly} from "../authentication";

export default class NavigationBar extends Component {
    render(){
        return <Authentication>
            {
                auth => <NavBar user = {auth.user.username}/>
            }
        </Authentication>
    }
}

export class NavBar extends Component {
    render(){
        return <Navbar sticky = "top" expand color="dark" >
            <NavbarBrand tag={Link} to="/">React app example</NavbarBrand>
            <Nav className="ml-auto" navbar>
                <NavItem>
                    <NavLink className="searchInput" tag={Link} to="/">Post list</NavLink>
                </NavItem>
                <AuthenticatedOnly requiredRole = "admin">
                    <NavItem>
                        <NavLink tag={Link} to="/admin">Admin</NavLink>
                    </NavItem>
                </AuthenticatedOnly>
                <UnauthenticatedOnly>
                    <NavItem>
                        <NavLink className="searchInput" tag={Link} to="/register">Register</NavLink>
                    </NavItem>
                </UnauthenticatedOnly>
                <UnauthenticatedOnly>
                    <NavItem>
                        <NavLink className="searchInput" tag={Link} to="/login">Login</NavLink>
                    </NavItem>
                </UnauthenticatedOnly>
                <AuthenticatedOnly>
                    <NavItem>
                        <NavLink className="searchInput" tag={Link} to="/logout">Logout</NavLink>
                    </NavItem>
                </AuthenticatedOnly>
            </Nav>
            <Form inline>
                <FormGroup>
                    <InputGroup size = "sm">
                        <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                                <SearchIcon/>
                            </InputGroupText>
                        </InputGroupAddon>
                        <Input type = "text" placeholder="Search..." />
                        <InputGroupAddon addonType="append">
                            <Button onClick={this.onSearchButtonClick}>Search</Button>
                        </InputGroupAddon>
                    </InputGroup>
                </FormGroup>
            </Form>
        </Navbar>
    }
}

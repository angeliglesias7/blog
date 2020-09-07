import React, { PureComponent as Component } from 'react'
import { Link } from 'react-router-dom'
import './styles.css';


export class PublicacionList extends Component {
    constructor(){
        super()
        this.state = {
            publicaciones : []
        }
    }

    async componentDidMount(){

        const publicacionesRequest = await fetch(`http://localhost:8081/publicaciones`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            }
        })
        const publicacionesResponse = await publicacionesRequest.json()

        this.setState(prev => ({...prev, publicaciones: publicacionesResponse}))


    }


    render() {
        return this.state.publicaciones.map(publicacion => <PublicacionPreview
            key = { publicacion.id }
            author = { publicacion.username }
            title = { publicacion.titulo }
            date = { publicacion.fechaPublicacion }
            resume = { publicacion.resumen }
            body = { publicacion.cuerpo }
            id = { publicacion.id } />
        )
    }
}

export class PublicacionPreview extends Component {
    render(){
        return <article>
            <header>
                <h2 className={"colorTitulo"}>{this.props.title}</h2>
                <h5>
                    <address>Autor: {this.props.author}</address>
                    Fecha: <time dateTime={this.props.date}>{this.props.date}</time>
                </h5>
            </header>
            <br></br>
            <h6 className="colorTextoComentario">{this.props.resume}</h6>
            <footer>
                <br></br>
                <Link to={`/posts/${this.props.id}`}>Ver más...</Link>
                <br></br>
                <br></br>
                <br></br>
            </footer>
        </article>
    }
}

export class PublicacionCompleta extends Component {
    constructor(){
        super()

        this.state = {
            id : "",
            author : "",
            date: "",
            title: "",
            summary: "",
            body: "",
            keyWords: [],
            comments: []
        }
    }

    async componentDidMount(){

        const publicacionesRequest = await fetch(`http://localhost:8081/publicaciones/${this.props.match.params.id}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            }
        })
        const publicacionesResponse = await publicacionesRequest.json()

        const comentariosRequest = await fetch(`http://localhost:8081/publicaciones/${this.props.match.params.id}/comentarios`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            }
        })
        const comentariosResponse = await comentariosRequest.json()

        this.setState(prev => ({ ...prev,
            id: publicacionesResponse.id,
            author: publicacionesResponse.username,
            date: publicacionesResponse.fechaPublicacion,
            title: publicacionesResponse.titulo,
            summary: publicacionesResponse.resumen,
            body: publicacionesResponse.cuerpo,
            keyWords: publicacionesResponse.palabrasClave,
            comments: comentariosResponse
        }))
    }

    render(){
        return <article>
            <header>
                <h2 className={"colorTitulo"}>{this.state.title}</h2>
                <h5>
                    <address>Autor: {this.state.author}</address>
                    Fecha: <time dateTime={this.state.date}>{this.state.date}</time>
                </h5>
            </header>
            <br></br>
            {this.state.body.split("\n").map((paragraph, index) => <h6 className="colorTextoComentario" key={`p-${index}`}>{paragraph}</h6>)}
            <br></br>
            <br></br>
            <br></br>
            <section>
                <header>
                    <h4>Comentarios</h4>
                    <hr/>
                </header>
                {this.state.comments.map(comment => <ComentarioPublicacion key={comment.id} usuario={comment.username}
                                                                           cuerpo={comment.comentario}/>)}
            </section>
        </article>
    }
}

export class ComentarioPublicacion extends Component {
    render() {
        return <article>
            <header>
                <h6 className="colorUsuario">{this.props.usuario}</h6>
            </header>
            {this.props.cuerpo.split("\n").map((paragraph, index) => <p className="colorTextoComentario" key={`c-${index}`}>{paragraph}</p>)}
        </article>
    }
}




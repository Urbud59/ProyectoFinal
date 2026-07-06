import { useState } from "react";
import "./Login.css";

function InicioSesion({ onLogin, volver }) {

    const [usuario, setUsuario] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const intentarLogin = async (url) => {

        try {

            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    usuario,
                    password
                })
            });

            const data = await response.json();
            return data;

        } catch {

            return null;

        }

    };

    const iniciarSesion = async () => {

        if (!usuario || !password) {
            setError("Complete todos los campos");
            return;
        }

        setError("");

        // 1. Intentar primero con Python (usuarios registrados)
        const dataPython = await intentarLogin("http://localhost:6060/api/login");

        if (dataPython && dataPython.success) {

            localStorage.setItem("usuario", dataPython.usuario);
            onLogin(dataPython.usuario);
            return;

        }

        // 2. Si falla, intentar con Scala (usuarios de prueba)
        const dataScala = await intentarLogin("http://localhost:8080/api/login");

        if (dataScala && dataScala.success) {

            localStorage.setItem("usuario", dataScala.usuario);
            onLogin(dataScala.usuario);
            return;

        }

        // 3. Si ninguno funcionó
        if (!dataPython && !dataScala) {
            setError("No se pudo conectar con los servidores (Python 6060 / Scala 8080)");
        } else {
            setError("Credenciales incorrectas");
        }

    };

    return (

        <div className="login-page">

            <div className="login-container">

                <h2>Iniciar Sesión</h2>

                <input
                    placeholder="Usuario"
                    value={usuario}
                    onChange={(e) => setUsuario(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button onClick={iniciarSesion}>
                    Entrar
                </button>

                <button
                    onClick={volver}
                    style={{
                        marginTop:10,
                        background:"#7f8c8d"
                    }}
                >
                    Volver
                </button>

                <p style={{color:"red"}}>
                    {error}
                </p>

            </div>

        </div>

    );

}

export default InicioSesion;

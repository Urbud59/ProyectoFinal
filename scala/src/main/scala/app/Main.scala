package app
import cask._
import upickle.default._
import app.services.InventarioService
import app.services.LoginService
object MyRoutes extends cask.Routes {
  // Headers CORS
  private val corsHeaders = Seq(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "GET, POST, OPTIONS",
    "Access-Control-Allow-Headers" -> "Content-Type"
  )
  // Manejo de peticiones OPTIONS (Preflight CORS)
  @cask.options("/api/login")
  def loginOptions() = {
    cask.Response(
      "",
      statusCode = 200,
      headers = corsHeaders
    )
  }
  @cask.options("/api/vender")
  def venderOptions() = {
    cask.Response(
      "",
      statusCode = 200,
      headers = corsHeaders
    )
  }
  // ENDPOINT 1: Obtener productos
  @cask.get("/api/productos")
  def obtenerProductos(): cask.Response[String] = {
    val listaProductos = InventarioService.obtenerTodos()
    val jsonResponse = write(listaProductos)
    cask.Response(
      data = jsonResponse,
      statusCode = 200,
      headers = Seq(
        "Content-Type" -> "application/json"
      ) ++ corsHeaders
    )
  }
  // ENDPOINT 2: Procesar venta
  @cask.post("/api/vender")
  def procesarVenta(request: cask.Request): cask.Response[String] = {
    try {
      val cuerpoInput = request.text()
      val dataJson = ujson.read(cuerpoInput)
      val idProducto = dataJson("id").str
      val mensajeResultado =
        InventarioService.vender(idProducto)
      if (mensajeResultado.contains("exitosamente")) {
        cask.Response(
          s"""{"status":"success","message":"$mensajeResultado"}""",
          statusCode = 200,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
      } else {
        cask.Response(
          s"""{"status":"error","message":"$mensajeResultado"}""",
          statusCode = 400,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
      }
    } catch {
      case e: Exception =>
        cask.Response(
          s"""{"status":"error","message":"${e.getMessage}"}""",
          statusCode = 500,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
    }
  }
  // ENDPOINT 3: Login
  @cask.post("/api/login")
  def login(request: cask.Request): cask.Response[String] = {
    try {
      val body = ujson.read(request.text())
      val usuario = body("usuario").str
      val password = body("password").str
      if (LoginService.autenticar(usuario, password)) {
        cask.Response(
          s"""{"success":true,"usuario":"$usuario"}""",
          statusCode = 200,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
      } else {
        cask.Response(
          """{"success":false,"message":"Credenciales incorrectas"}""",
          statusCode = 401,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
      }
    } catch {
      case e: Exception =>
        cask.Response(
          s"""{"success":false,"message":"${e.getMessage}"}""",
          statusCode = 500,
          headers = Seq(
            "Content-Type" -> "application/json"
          ) ++ corsHeaders
        )
    }
  }
  initialize()
}
object Main extends cask.Main {
  override def allRoutes = Seq(MyRoutes)
  override def port: Int = sys.env.get("PORT").map(_.toInt).getOrElse(8080)
  override def host: String = "0.0.0.0"
  override def main(args: Array[String]): Unit = {
    super.main(args)
    println(
      "🚀 Servidor Backend en Scala ejecutándose..."
    )
    while (true) {
      Thread.sleep(10000)
    }
  }
}

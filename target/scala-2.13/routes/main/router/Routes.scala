// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.models._

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  Assets_0: controllers.Assets,
  // @LINE:10
  HomeController_1: controllers.HomeController,
  // @LINE:22
  SessionController_2: controllers.SessionController,
  val prefix: String
) extends GeneratedRouter {

  @javax.inject.Inject()
  def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    Assets_0: controllers.Assets,
    // @LINE:10
    HomeController_1: controllers.HomeController,
    // @LINE:22
    SessionController_2: controllers.SessionController
  ) = this(errorHandler, Assets_0, HomeController_1, SessionController_2, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, Assets_0, HomeController_1, SessionController_2, prefix)
  }

  private val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """session/start""", """controllers.HomeController.startSession"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """session/stop""", """controllers.HomeController.stopSession"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """addLap/""" + "$" + """sessionId<[^/]+>""", """controllers.HomeController.addLap(sessionId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """sessions/""" + "$" + """userId<[^/]+>""", """controllers.SessionController.getSessionsForUser(userId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """test-db-connection""", """controllers.HomeController.testDbConnection"""),
    Nil
  ).foldLeft(Seq.empty[(String, String, String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String, String, String)]
    case l => s ++ l.asInstanceOf[List[(String, String, String)]]
  }}


  // @LINE:6
  private lazy val controllers_Assets_versioned0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""", encodeable=false)))
  )
  private lazy val controllers_Assets_versioned0_invoker = createInvoker(
    Assets_0.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Static Assets
 Route to serve static files like stylesheets, scripts, and images.""",
      Seq()
    )
  )

  // @LINE:10
  private lazy val controllers_HomeController_index1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private lazy val controllers_HomeController_index1_invoker = createInvoker(
    HomeController_1.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ Home Page
 Route for the main home page.""",
      Seq()
    )
  )

  // @LINE:14
  private lazy val controllers_HomeController_startSession2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("session/start")))
  )
  private lazy val controllers_HomeController_startSession2_invoker = createInvoker(
    HomeController_1.startSession,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "startSession",
      Nil,
      "POST",
      this.prefix + """session/start""",
      """ Session API
 Route to start a new session.""",
      Seq()
    )
  )

  // @LINE:16
  private lazy val controllers_HomeController_stopSession3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("session/stop")))
  )
  private lazy val controllers_HomeController_stopSession3_invoker = createInvoker(
    HomeController_1.stopSession,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "stopSession",
      Nil,
      "POST",
      this.prefix + """session/stop""",
      """ Route to stop an existing session.""",
      Seq()
    )
  )

  // @LINE:18
  private lazy val controllers_HomeController_addLap4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("addLap/"), DynamicPart("sessionId", """[^/]+""", encodeable=true)))
  )
  private lazy val controllers_HomeController_addLap4_invoker = createInvoker(
    HomeController_1.addLap(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "addLap",
      Seq(classOf[String]),
      "POST",
      this.prefix + """addLap/""" + "$" + """sessionId<[^/]+>""",
      """ Route to add a lap to a session by its session ID.""",
      Seq()
    )
  )

  // @LINE:22
  private lazy val controllers_SessionController_getSessionsForUser5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("sessions/"), DynamicPart("userId", """[^/]+""", encodeable=true)))
  )
  private lazy val controllers_SessionController_getSessionsForUser5_invoker = createInvoker(
    SessionController_2.getSessionsForUser(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SessionController",
      "getSessionsForUser",
      Seq(classOf[String]),
      "GET",
      this.prefix + """sessions/""" + "$" + """userId<[^/]+>""",
      """ Fetch Sessions
 Route to fetch all sessions for a specific user by their userId.""",
      Seq()
    )
  )

  // @LINE:26
  private lazy val controllers_HomeController_testDbConnection6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("test-db-connection")))
  )
  private lazy val controllers_HomeController_testDbConnection6_invoker = createInvoker(
    HomeController_1.testDbConnection,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "testDbConnection",
      Nil,
      "GET",
      this.prefix + """test-db-connection""",
      """ Database Testing
 Route to test the database connection.""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_Assets_versioned0_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned0_invoker.call(Assets_0.versioned(path, file))
      }
  
    // @LINE:10
    case controllers_HomeController_index1_route(params@_) =>
      call { 
        controllers_HomeController_index1_invoker.call(HomeController_1.index)
      }
  
    // @LINE:14
    case controllers_HomeController_startSession2_route(params@_) =>
      call { 
        controllers_HomeController_startSession2_invoker.call(HomeController_1.startSession)
      }
  
    // @LINE:16
    case controllers_HomeController_stopSession3_route(params@_) =>
      call { 
        controllers_HomeController_stopSession3_invoker.call(HomeController_1.stopSession)
      }
  
    // @LINE:18
    case controllers_HomeController_addLap4_route(params@_) =>
      call(params.fromPath[String]("sessionId", None)) { (sessionId) =>
        controllers_HomeController_addLap4_invoker.call(HomeController_1.addLap(sessionId))
      }
  
    // @LINE:22
    case controllers_SessionController_getSessionsForUser5_route(params@_) =>
      call(params.fromPath[String]("userId", None)) { (userId) =>
        controllers_SessionController_getSessionsForUser5_invoker.call(SessionController_2.getSessionsForUser(userId))
      }
  
    // @LINE:26
    case controllers_HomeController_testDbConnection6_route(params@_) =>
      call { 
        controllers_HomeController_testDbConnection6_invoker.call(HomeController_1.testDbConnection)
      }
  }
}

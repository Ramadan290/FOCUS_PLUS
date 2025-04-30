// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.models._

// @LINE:6
package controllers.javascript {

  // @LINE:6
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:10
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def stopSession: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.stopSession",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "session/stop"})
        }
      """
    )
  
    // @LINE:26
    def testDbConnection: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.testDbConnection",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "test-db-connection"})
        }
      """
    )
  
    // @LINE:14
    def startSession: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.startSession",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "session/start"})
        }
      """
    )
  
    // @LINE:18
    def addLap: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.addLap",
      """
        function(sessionId0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "addLap/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("sessionId", sessionId0))})
        }
      """
    )
  
    // @LINE:10
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }

  // @LINE:22
  class ReverseSessionController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def getSessionsForUser: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SessionController.getSessionsForUser",
      """
        function(userId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "sessions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("userId", userId0))})
        }
      """
    )
  
  }


}

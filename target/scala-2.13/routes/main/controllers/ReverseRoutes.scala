// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.models._

// @LINE:6
package controllers {

  // @LINE:6
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:10
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def stopSession: Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "session/stop")
    }
  
    // @LINE:26
    def testDbConnection: Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "test-db-connection")
    }
  
    // @LINE:14
    def startSession: Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "session/start")
    }
  
    // @LINE:18
    def addLap(sessionId:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "addLap/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("sessionId", sessionId)))
    }
  
    // @LINE:10
    def index: Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:22
  class ReverseSessionController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def getSessionsForUser(userId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "sessions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("userId", userId)))
    }
  
  }


}

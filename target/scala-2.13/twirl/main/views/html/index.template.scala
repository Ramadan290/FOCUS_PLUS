
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._
import models._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Seq[models.Session],String,play.api.mvc.RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(sessions: Seq[models.Session], title: String)(implicit request: play.api.mvc.RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.94*/("""

"""),format.raw/*3.1*/("""<html>
<head>
    <title>"""),_display_(/*5.13*/title),format.raw/*5.18*/("""</title>
    <link rel="stylesheet" href=""""),_display_(/*6.35*/routes/*6.41*/.Assets.versioned("stylesheets/main.css")),format.raw/*6.82*/("""">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <script src=""""),_display_(/*8.19*/routes/*8.25*/.Assets.versioned("javascripts/main.js")),format.raw/*8.65*/("""" defer></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <meta name="csrfToken" content=""""),_display_(/*10.38*/play/*10.42*/.filters.csrf.CSRF.getToken.map(_.value).getOrElse("")),format.raw/*10.96*/("""">


</head>
<body>
    <!-- Header Section -->
    <div class="header">
        <button id="pomodoro-button" class="mode-button" onclick="startPomodoro()">Pomodoro</button>
        <button id="time-boxing-button" class="mode-button" onclick="startTimeBoxing()">Time Boxing</button>
        <button id="feynman-button" class="mode-button" onclick="startFeynman()">Feynman</button>
    </div>

    <!-- Layout -->
    <div class="layout-container">
        <!-- Buttons Section -->
        <div class="button-container">
            <!-- Start/Pause button toggles icon dynamically -->
            <button id="start-button" class="btn-primary" onclick="toggleTimer()">
                <i class="fas fa-play" id="start-icon"></i>
            </button>
            <!-- Lap Button -->
            <button id="lap-button" class="btn-secondary" onclick="addLap()">
                <i class="fas fa-flag"></i>
            </button>
            <!-- Stop Button -->
            <button id="stop-button" class="btn-danger" onclick="stopTimer()">
                <i class="fas fa-stop"></i>
            </button>
        </div>

        <!-- Timer Section -->
        <div class="timer-container">
            <div class="circle-timer">
                <svg viewBox="0 0 100 100">
                    <circle class="timer-background" cx="50" cy="50" r="45"></circle>
                    <circle class="timer-progress" cx="50" cy="50" r="45"></circle>
                </svg>
                <div class="time">
                    <span id="minutes">00</span>:<span id="seconds">00</span>
                </div>
            </div>
        </div>

        <!-- Laps and Streaks Section -->
        <div class="laps-streaks-box">
            <h3>Laps & Streaks</h3>
            <div id="laps-streaks-list">
                <!-- Laps and streaks will be dynamically added here -->
            </div>
            <!-- Clear Laps Button -->
        </div>
        <!-- Session Details Section -->
            <!-- Session History Section (This is from sessions.html) -->
            <h3 class="session-history-title">Session History</h3>

    <div class="session-history-container">
        <div class="session-history">
            <ul>
                """),_display_(/*68.18*/for(session <- sessions) yield /*68.42*/ {_display_(Seq[Any](format.raw/*68.44*/("""
                    """),format.raw/*69.21*/("""<li>
                        <div><strong>Start Time:</strong> """),_display_(/*70.60*/session/*70.67*/.startTime),format.raw/*70.77*/("""</div>
                        <div><strong>End Time:</strong> """),_display_(/*71.58*/session/*71.65*/.endTime),format.raw/*71.73*/("""</div>
                        <div><strong>Duration:</strong> """),_display_(/*72.58*/session/*72.65*/.duration),format.raw/*72.74*/("""</div>
                        <div><strong>Laps:</strong> """),_display_(/*73.54*/session/*73.61*/.laps),format.raw/*73.66*/("""</div>
                        <div><strong>Streaks:</strong> """),_display_(/*74.57*/session/*74.64*/.streaks),format.raw/*74.72*/("""</div>
                    </li>
                """)))}),format.raw/*76.18*/("""
            """),format.raw/*77.13*/("""</ul>
        </div>
        </div>
        <button class="toggle-session-button" onclick="toggleSessionHistory()"><i class='fas fa-angle-double-down'></i></button>

    </div>
        <!-- Break Warning Modal -->
        <div id="break-warning-modal" class="modal">
            <div class="modal-content">
                <h2>Take a Brea k!</h2>
                <p>Pause for 5 mins and relax before continuing.</p>
                <button id="close-warning-modal" class="modal-button">OK</button>
            </div>
        </div>
    </body>
    </html>
</body>
</html>
"""))
      }
    }
  }

  def render(sessions:Seq[models.Session],title:String,request:play.api.mvc.RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(sessions,title)(request)

  def f:((Seq[models.Session],String) => (play.api.mvc.RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (sessions,title) => (request) => apply(sessions,title)(request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: 883ab36553460320d73bee80923aea13ec07c7bc
                  MATRIX: 792->1|979->93|1009->97|1063->125|1088->130|1158->174|1172->180|1233->221|1397->359|1411->365|1471->405|1673->580|1686->584|1761->638|4085->2935|4125->2959|4165->2961|4215->2983|4307->3048|4323->3055|4354->3065|4446->3130|4462->3137|4491->3145|4583->3210|4599->3217|4629->3226|4717->3287|4733->3294|4759->3299|4850->3363|4866->3370|4895->3378|4978->3430|5020->3444
                  LINES: 22->1|27->1|29->3|31->5|31->5|32->6|32->6|32->6|34->8|34->8|34->8|36->10|36->10|36->10|94->68|94->68|94->68|95->69|96->70|96->70|96->70|97->71|97->71|97->71|98->72|98->72|98->72|99->73|99->73|99->73|100->74|100->74|100->74|102->76|103->77
                  -- GENERATED --
              */
          
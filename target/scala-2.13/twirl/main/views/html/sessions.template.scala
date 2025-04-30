
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

object sessions extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Seq[models.Session],String,play.api.mvc.RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

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
    <meta name="csrfToken" content=""""),_display_(/*9.38*/play/*9.42*/.filters.csrf.CSRF.getToken.map(_.value).getOrElse("")),format.raw/*9.96*/("""">


</head>
<body>
    <div class="session-history-container">
        <h1>"""),_display_(/*15.14*/title),format.raw/*15.19*/("""</h1>

        <!-- Session History -->
        <div class="session-history">
            <h3>Session History</h3>
            <ul>
                """),_display_(/*21.18*/for(session <- sessions) yield /*21.42*/ {_display_(Seq[Any](format.raw/*21.44*/("""
                    """),format.raw/*22.21*/("""<li>
                        <div><strong>Start Time:</strong> """),_display_(/*23.60*/session/*23.67*/.startTime),format.raw/*23.77*/("""</div>
                        <div><strong>End Time:</strong> """),_display_(/*24.58*/session/*24.65*/.endTime.getOrElse("Ongoing")),format.raw/*24.94*/("""</div>
                        <div><strong>Duration:</strong> """),_display_(/*25.58*/session/*25.65*/.duration.getOrElse(0)),format.raw/*25.87*/(""" """),format.raw/*25.88*/("""seconds</div>
                        <div><strong>Laps:</strong> """),_display_(/*26.54*/session/*26.61*/.laps),format.raw/*26.66*/("""</div>
                        <div><strong>Streaks:</strong> """),_display_(/*27.57*/session/*27.64*/.streaks),format.raw/*27.72*/("""</div>
                    </li>
                """)))}),format.raw/*29.18*/("""
            """),format.raw/*30.13*/("""</ul>
        </div>
    </div>
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
                  SOURCE: app/views/sessions.scala.html
                  HASH: c1779a5aba62fcc351a027c6a1ee867efa66ecb0
                  MATRIX: 795->1|982->93|1012->97|1066->125|1091->130|1161->174|1175->180|1236->221|1400->359|1414->365|1474->405|1556->461|1568->465|1642->519|1752->602|1778->607|1960->762|2000->786|2040->788|2090->810|2182->875|2198->882|2229->892|2321->957|2337->964|2387->993|2479->1058|2495->1065|2538->1087|2567->1088|2662->1156|2678->1163|2704->1168|2795->1232|2811->1239|2840->1247|2923->1299|2965->1313
                  LINES: 22->1|27->1|29->3|31->5|31->5|32->6|32->6|32->6|34->8|34->8|34->8|35->9|35->9|35->9|41->15|41->15|47->21|47->21|47->21|48->22|49->23|49->23|49->23|50->24|50->24|50->24|51->25|51->25|51->25|51->25|52->26|52->26|52->26|53->27|53->27|53->27|55->29|56->30
                  -- GENERATED --
              */
          
import scala.scalajs.js.JSApp

object Hello extends JSApp {
  val baseUrl: BaseUrl = BaseUrl.until_#

  val router = Router(baseUrl, ClientRoutes.routesConfig)

  override def main(): Unit = {
    // create stylesheet
    GlobalStyles.addToDocument()

    RankingCircuit.dispatch(InitProducts)
    router().render(dom.document.getElementsByClassName("root")(0))
  }
}
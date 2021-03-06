package view.fxview.component.manager.subcomponent.parent

trait ManagerHomeParent extends FillHolesParent with ManagerRichiestaParent with ChooseParamsParent with ModalParamParent
  with ChangeSettimanaRichiestaParent with GroupParamsParent with SelectResultParent with ModalRunParent
  with ModalGruopParent with ShowParamAlgorithmBoxParent with ZonaParent with TerminalParent{

  /**
   * method that allow draw principal view [[view.fxview.component.manager.subcomponent.ResultBox]]
   */
  def drawResultPanel(): Unit

  /**
   * call view for draw view [[view.fxview.component.manager.subcomponent.DateAndTerminalBox]]
   */
  def drawRichiestaPanel(): Unit

  /**
   * Method used when is needed to draw the absence panel
   */
  def drawAbsencePanel(): Unit

  /**
   * Method used to draw the panel to choose params for the algorithm
   */
  def drawParamsPanel(): Unit

  /**
   * It notify parent that a manage zones view must be shown
   *
   */
  def drawZonePanel(): Unit

  /**
   * It notify parent that a manage terminal view must be shown
   *
   */
  def drawTerminalPanel(): Unit

}

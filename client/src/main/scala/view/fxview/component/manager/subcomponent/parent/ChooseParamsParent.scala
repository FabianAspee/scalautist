package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.Terminale
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm

/**
 * @author Francesco Cassano
 * This trait allow to send request to main view
 */
trait ChooseParamsParent {

  /**
   * Method allows to draw the modal for choosing params
   * @param terminals List of [[caseclass.CaseClassDB.Terminale]]
   */
  def modalOldParam(terminals: List[Terminale]): Unit

  /**
   * Method that allows to draw the panel to set weeks params
   */
  def weekParams(param: ParamsForAlgoritm): Unit

}

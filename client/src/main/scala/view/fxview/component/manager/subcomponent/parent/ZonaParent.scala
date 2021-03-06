package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.Zona

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by zona views to make requests to controller
 */
trait ZonaParent {

  /**
   * Add a new Zona in the db
   *
   * @param zona
   *             [[caseclass.CaseClassDB.Zona]] instance to add
   */
  def newZona(zona: Zona): Unit

  /**
   * open a modal
   *
   * @param zona
   *             [[caseclass.CaseClassDB.Zona]] instance to manage
   */
  def openZonaModal(zona: Zona): Unit

}

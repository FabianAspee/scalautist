package controller

import java.sql.Date

import caseclass.CaseClassDB.Persona
import model.entity.HumanResourceModel
import view.fxview.mainview.HumanResourceView

/**
 * @author Francesco Cassano
 *
 * A HumanResource controller for a view of type [[view.fxview.mainview.HumanResourceView]]
 */
trait HumanResourceController extends AbstractController[HumanResourceView] {

  /**
   * Recruit saves a new employee on the db
   *
   * @param persona
   *                instance of the employee to save. It's [[Persona]] instance //todo
   */
  def recruit(persona:Persona): Unit

  /**
   * Fires deletes one or more employee from the db
   *
   * @param ids
   *            Set of integer that represent employees' ids
   */
  def fires(ids:Set[Int]): Unit

  /**
   * getAllPersona asks model for the employees list
   */
  def getAllPersona: Unit //TODO quando i dati arrivano li faccio disegnare

  /**
   * illness saves on the db an employee's absence for a period of time
   *
   * @param idPersona
   *                  It's the employee's id
   * @param startDate
   *                  The date the desease starts
   * @param endDate
   *                The date the desease ends
   */
  def illness(idPersona: Int, startDate: Date, endDate: Date): Unit

  /**
   * Holiday saves on the db an employee's absence for a period of time
   *
   * @param idPersona
   *                  It's the employee's id
   * @param startDate
   *                  The date the desease starts
   * @param endDate
   *                The date the desease ends
   */
  def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit

  /**
   * PasswordRecovery asks the system for create new credential for a user
   * @param user
   *             User's id
   */
  def passwordRecovery(user: Int): Unit //TODO quando i dati arrivano li faccio disegnare
}

/**
 * @author Francesco Cassano
 *
 * Companion object of [[controller.HumanResourceController]] [Singleton]
 *
 */
object HumanResourceController {
  private val instance = new HumanResourceControllerImpl()
  private val model = HumanResourceModel()

  def apply(): HumanResourceController = instance

  /**
   * HumanResourceController implementation
   */
  private class HumanResourceControllerImpl extends HumanResourceController {

    override def recruit(persona: Persona): Unit = model.recruit(persona)

    override def fires(ids: Set[Int]): Unit = model.fires(ids)

    override def getAllPersona: Unit = model.getAllPersone

    override def illness(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.illnessPeriod(idPersona, startDate,endDate)

    override def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.holidays(idPersona, startDate, endDate)

    override def passwordRecovery(user: Int): Unit = model.passwordRecovery(user)
  }
}

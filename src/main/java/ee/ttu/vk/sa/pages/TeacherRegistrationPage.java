package ee.ttu.vk.sa.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;
import ee.ttu.vk.sa.CustomAuthenticatedWebSession;
import ee.ttu.vk.sa.domain.Teacher;
import ee.ttu.vk.sa.pages.panels.TeacherRegistrationPanel;
import ee.ttu.vk.sa.pages.providers.TeacherDataProvider;
import ee.ttu.vk.sa.service.TeacherService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by vadimstrukov on 2/15/16.
 */
@AuthorizeInstantiation(Roles.ADMIN)
public class TeacherRegistrationPage extends AbstractPage {


    @SpringBean
    private TeacherService teacherService;
    private DataView<Teacher> teachers;
    private TeacherDataProvider teacherDataProvider;
    private WebMarkupContainer teacherTable;
    private TeacherRegistrationPanel registrationPanel;


    public TeacherRegistrationPage(){
        teacherDataProvider = new TeacherDataProvider();
        teacherTable = new WebMarkupContainer("teacherTable");
        teacherTable.setOutputMarkupId(true);
        teachers = getTeachers();
        teachers.setItemsPerPage(10);
        teacherTable.add(teachers);
        registrationPanel = new TeacherRegistrationPanel("teacherPanel", new CompoundPropertyModel<>(new Teacher()));
        add(teacherTable, new BootstrapAjaxPagingNavigator("navigator", teachers), registrationPanel, getButtonAddTeacher());
    }


    private DataView<Teacher> getTeachers(){
        return new DataView<Teacher>("teachers", teacherDataProvider) {
            @Override
            protected void populateItem(Item<Teacher> item) {
                item.add(new AjaxLink<Teacher>("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        registrationPanel.setModel(item.getModel());
                        ajaxRequestTarget.add(registrationPanel);
                        registrationPanel.appendShowDialogJavaScript(ajaxRequestTarget);
                    }

                    @Override
                    public boolean isEnabled() {
                        return CustomAuthenticatedWebSession.getSession().isSignedIn();
                    }
                }.add(new Label("name")));
                item.add(new Label("email"));
                item.add(new Label("password"));
                item.add(new AjaxLink<Teacher>("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        teacherService.deleteTeacher(item.getModelObject());
                        ajaxRequestTarget.add(teacherTable);
                    }

                    @Override
                    public boolean isVisible() {
                        return CustomAuthenticatedWebSession.getSession().isSignedIn();
                    }
                });
            }
        };
    }

    private AjaxLink<Teacher> getButtonAddTeacher(){
        return new AjaxLink<Teacher>("addTeacher") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                registrationPanel.setModel(new CompoundPropertyModel<>(new Teacher()));
                ajaxRequestTarget.add(registrationPanel);
                registrationPanel.appendShowDialogJavaScript(ajaxRequestTarget);
            }
            @Override
            public boolean isEnabled() {
                return CustomAuthenticatedWebSession.getSession().isSignedIn();
            }
        };
    }
}

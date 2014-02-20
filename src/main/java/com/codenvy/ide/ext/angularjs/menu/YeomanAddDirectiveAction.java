package com.codenvy.ide.ext.angularjs.menu;

import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Florent Benoit
 */
public class YeomanAddDirectiveAction extends Action {

    private BuildProjectPresenter buildProjectPresenter;

    private DtoFactory dtoFactory;

    @Inject
    public YeomanAddDirectiveAction(LocalizationConstant localizationConstant, BuildProjectPresenter buildProjectPresenter, DtoFactory dtoFactory) {
        super(localizationConstant.yeomanAddDirectiveText(), localizationConstant.runAppActionDescription(), null);
        this.buildProjectPresenter = buildProjectPresenter;
        this.dtoFactory = dtoFactory;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the result through a prompt
        String directiveName = "toto";

        build("angular:directive", directiveName);
    }



    protected void build(String... parameters) {
        List<String> targets = Arrays.asList(parameters);
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("yeoman");
        buildProjectPresenter.buildActiveProject(buildOptions);
    }
}

package com.psp.model.view;

public class JsView {
    public class PublicView{}
    public class StudentView extends PublicView{ }
    public class CompanyView extends PublicView{ }
    public class CompanyStarView extends CompanyView{ }
    public class CompanyLetterView extends CompanyView{ }
    public class StudentResumeView extends StudentView{ }
    public class StudentProjectsView extends StudentView{ }
}

# Changelog

## [06.26.250115]

### Changes
- Added Middle name in the users table.
- Faculty/Student Dashboard Name display changes.
- Faculty/Student Profile Page Name display changes.

## [06.25.250330]

### Changes
- Removed yearLevel field that is not connectd to enrollment table.

### Fix
- Fixed User Management Page Modal not populating the program and passwords.

### Bugs
- User Management Page View Modal not displaying the correct informations.
- User Management Page Last Login is not fetching in the table

### Future Changes
- Add Middlename field in the User Creation.
- Update all the name on the system to display the Middlename on Fullnames.
  
## [06.25.250225]

### Fix
- Fixed Student Schedule Page Table.

## [06.25.250203]

### New
- Working Pending Approval in the Admin Dashboard.

## [06.25.250057]

### Changes
- Removed System Notification Section in the Admin Dashboard.

## [06.25.250033]

### New
- Added Bulk Enrollment Feature in the Enrollment Management Page [ADMIN].

### Changes
- Removed ref links of common.css in all html files.

## [06.24.252241]

### Changes
- Notification Feature for the Admin when student requests for enrollment.
- Improve the Notifications UI.

### Added
- Added System Notifications in the Student and Faculty Home/Dashboard.

## [06.24.251809]

### Changes
- Notification for Faculty when assigned or remove from assignned course is now working.

## [06.24.250247]

### Changes
- Improved Faculty Grading Sheets.
- Improved Faculty Profile Page.
  - Added New fields in the Faculty Entity Model.
- Improved Faculty Schedule Page.
- Improved Faculty Workload Page.

## [06.23.252230]

### Changes
- Slightly Improved Faculty Pages.
- Slightly Cleaned the Home.css file.

## [06.23.251936]

### Changes
- Added delete notification 

## [06.23.251811]

### New
- Notification feature for the Students. Enrollments and Grades Encoding.

## [06.23.251652]

### Changes
- Admin Pages HTML Header Changes
- Admin Pages Icon Changes

## [06.23.250104]

### Changes
- Separated the Header and Footer HTML for reusability.
- Student Pages Improvements.
- Log In page UI changes.

## [06.22.250147]

### Changes
- Login Page UI Changes

### Removed
- Removed student-enrollment separate js file.
- Removed forgot-password html file.
- Removed mapping for forgot password.

## [06.22.250129]

### New
- Added a feature where when the System will automatically create an admin account when there accounts of it.

## [06.21.250125]

### Major Changes
- Self Enrollment of students is now Working.

## [06.20.251456]

### Major Changes
- Working Grading Sheet Page for Faculty.
- Working Grades Page for Student

## [06.20.250229]

### Changes
- Admin Assignment Schedule Page now has a School Year option.

### Fix
- Faculty Dashboard Page.
- Faculty Workload Page.

### Bugs
- Faculty Workload Page Filter for School Year and Semester is not working.

## [06.19.252215]

### Fix
- Student Schedule Page now displays all the 

## [06.19.252137]

### Changes
- Schedule Management Page now fully Functional.
- Formatted lines of some Files.

## [06.19.252032]

### Fix
- Schedule Management Page is now working.

### Bug
- The remove assignment button is not working.

## [06.19.251657]

### Changes
- Added Curriculum Selection in the Section Management Page.

## [06.19.251619]

### Added
- FacultyAssignment backend files.
- Added new DTO files.

### Removed
- SectionCourse backend files.

### Bug
- Schedule Management Page Bug

## [06.18.252036]

### Minor Fix
- Formatted lines admin-section file
- Formatted lines admin-courses file

### Bug
- Schedule Management Bug

## [06.18.251444]

### Fix
- Fixed Enrollment Management BUGS.

### Bugs
- - Some Bugs from version [06.17.251702] still exist.

## [06.18.250201]

### Fix
- Fixed Curriculum Mangement BUGS.

### Bugs
- Some Bugs from version [06.17.251702] still exist.

## [06.17.251933]

### Fix
- Fixed Section Management BUGS.

### Bugs
- Other Bugs from last version except Section Management still exist.

## [06.17.251702]

### Changes
- Minor Changes

### Bugs
- Section Management Page
  - Not displaying all the sections.
- Enrollment Management Page
  - Enrolling the Student is Working and Adds the informatio in the Database. 
  - [PROBLEM] Can't display all the enrollment information of the students **PENDING/APPROVED/DECLINE.
- Curriculum Management Page
  - Adding a Curriculum is not Working.
- Schedule Assignment Page
  - The page is not Fully Functionable, but it supports loading of already assigned courses in the Database.


## [06.16.251505]

### Changes
- Schedule Assignment Page Changes [ADMIN]

## [06.16.251245]

### Changes
- Student pages API fixes. [STUDENT]
- Added Student Model, Entity, Repository.

## [06.16.251020]

### Changes
- Enrollment Fixes [ADMIN]

## [06.16.250904]

### Changes
- Enrollment Management Page UI Improvements

## [06.16.250830]

### Changes
- Working Curriculum Management Page [ADMIN]

## [06.15.252243]

### Major Changes
- Improved all the MODEL Entities in the System
-  Major FRONTEND & BACKEND Changes [ADMIN]
-  Added New files 

## [06.14.251118]

### Changes
- Removed Year Level and Semester in the Section's Frontend and Backend

## [06.13.251215]

### Changes
- Admin Dashboard System Summary Improvements

## [06.13.250208]

### ReCommit

## [06.13.250207]

### Changes
- Student Page Changes
  - Home Page Improvements
  - Schedule Page Improvements
  - Grades Page Improvements
  - Profile Page Improvements
  - 
 ### Bugs
 - White Label Error in the Enrollment Page

## [06.13.250025]

### Changes
- Student Pages Optimization

## [06.12.252301]

### Changes
- Improve Enrollment Feature in the User Management Page [ADMIN]

## [06.12.252110]

### Changes
- Supports deletion of Student Accounts even if Enrolled

## [06.12.252025]

### Changes
- Added Section to User Mangement Page [ADMIN].
- Filtering Courses in the modal based on the selected Program, YearLevel, and Semester.

## [06.12.251626]

### Changes
- Enrollment Feature [BETA].
- UI Changes on some of the admin pages.

## [06.12.251307]

### Changes
- Removed Faculty Evaluation [ADMIN].
- Removed Enrollment Page.
- Fixed the BUG that prevents from deleting a section in the Section Management Page [ADMIN].

## [06.12.250241]

### Changes
- Minor File Changes
- The password now support BCrypt

## [06.11.252152]

### Changes
- Program Management changed to Programs in the sidebar navigation.
- The Section Management Page now shows it's status info.
- The Course Management Page now shows it's semester info.

## [06.11.252022]

### Changes
- Functionable Section Management Page

### Bugs
- The logic of limiting the slots/capacity based on what is set, is still not working

## [06.11.251629]

### Changes
- Mimor UI Changes in the Action Buttons [ADMIN]

## [06.11.251529]

### Changes
- Minor Action Button UI Improvements in the User Management Page [ADMIN]

## [06.10.250143]

### Changes (Course Management Page)
- Added Semester Feature.
- Added Program, Semester Filter alongside Year Level for easier Access.
- Fixed the Selection of Program.
- Removed Course Description.

## [06.09.251339]

### Fixes
- Fixed the Year Level and Program Not Being AutoFilled as the admin enter the specific StudentID [ADMIN]

## [06.09.251309]

### Fixes
- Fixed the Year Level in the User Management Page [ADMIN]

## [06.09.250248]

### Changes
- New Enrollment Page [BETA]

## [06.09.250036]

### Changes
- Sidebar Navigation Fix

## [06.05.250113]

### Changes
- Course Management Page is now Fully Functionable and is connected to the Program Enity Model

### To Improve
- Admin Pages (Course, User, Program, etc.)

## [06.04.252213]

### Changes
- Course Management Page Changes

## [06.04.252026]

### Changes
- Improvement in the Program Management Page [ADMIN]
- Slight Improvement in the User Management Page [ADMIN]

## [06.02.251336]

### Changes
- Format the Whole Codebase

## [06.02.251321]

### Changes
- Temporarily Removed some of the Not Used Backend Files
- Created the Program Management Page
- Connects the Programs to the User 

## [06.02.250218]

### Changes
- User Management Page Improvement 

## [06.01.252109]

### Changes
- Removal of Department in the User Management Page
- Deleted some files (HTML)

## [06.01.252109]

### Changes
- Removed the Department in the Admin and some in the Faculty Pages

## [06.01.252242]

### Changes
- Removed Subject Management Page
- All Named Subject in the Page Renamed to Course

## [05.30.250126]

### Changes
- Removed the Department Status

### Bugs
- The Edit feature in the Department Management Page is still not working

## [05.29.251308]

### Changes
- Fixed some of the UI in the Department Management Page [ADMIN]

## [05.29.251205]

### Changes
- Fixed the UI of the Action Buttons in the Department Management Page [ADMIN]

## [05.28.251453]

### Added
- Department Management Page [ADMIN]

## [05.28.251338]

### Fixes
- Fixed the Faculty Information Correctly Displays based on who logged in

## [05.28.251040]

### Fixes
- Fixed the Last Login Feature

 ### Added
 - Added the FacultyID and StudentID Feature

## [05.23.251403]

### Changes
- Changes in the Admin Pages

## [05.23.251321]

### Changes
- Changes in the Admin Pages

## [05.23.251030]

### Changes
- Changes in the Admin Pages

## [05.22.251333]

### Changes
- Separated the Courses, Enrollment, sections, Grades to have their own Webpages

### Bugs
- Enrollment Page have the same page as the Grades

## [05.22.251128]

### Changes
- Improved Faculty Pages

## [05.19.251041]

### Removed
- Removed the Hardcoded Data in the Student Side

### Fixes
- Fixes some of the error after removing the Hardcoded Data

## [05.19.250104]

### Changes
- Minor Changes in the User Management Page[ADMIN]

## [05.18.252028]

### Changes
- Made the User Management Fully Functionable[ADMIN]

 ### Fixed
 - Fixed User Roles in the Config

## [05.18.251737]

### Added
- Working Account Login Credentials Based on the User Management Page[ADMIN]

## [05.18.251717]

### Added
- Adding a new User is now possible in the User Management[ADMIN]

## [05.15.251317]

### Changes
- Student Mapping Minor Changes

## [05.15.251224]

### Changes
- Changes to the Student Home Page

### Removed
- Removed the Secondary Navigation Page in the Student Home Page

## [05.15.251112]

### Changes
- Temporarily Changed the Background Image in all of Pages
- Renamed All the HTML files of Students

### Removed
- Removed Vertical Scroll Bar when Scrolling in all of pages for Design Contsistency
- Removed Profile, Settings, Reports in the Admin Page
- Temporarily Removed the Login credentials for all users

## [05.14.251650]

### Changes
- Improved Admin Pages Design

## [05.13.252048]

### Changes
- MySQL Database Connection

## [05.13.251109]

### Changes
- Remove the Main Navigation Bar
- Added Side Panel Bar 

### Added
- Admin Dashboard
- Admin Course/Section Management
- Admin Subject Management
- Admin Enrollment and Grade Management [beta]
- Admin User Management [beta]

[beta]
- Changes need to be made in the Side panel bar

## [05.12.250048]

#### Added
- Admin User Management

### Changes
- Remove Hardcoded Data in Admin Dashboard
- Remove Hardcoded Data in Admin User Management

## [05.08.250012]

### Fixes
- Working Login credentials for ADMIN, FACULTY, and STUDENT

## [05.07.251246]

### Changes
- Folder created for HTML of Faculty Student and Admin. Also for Global Files
- Change in the Controllers to match the pathing of the new folders for admin, student, and faculty
- Grades Year and Semester Selection Buttons

### Added
- Faculty Dashboard Page
- Faculty Workload Page
- Faculty Grading Sheet Page
- Faculy Controller

- Admin Dashboard Page
- Admin Controller

- Renamed HomeController to Student Controller

## [04.24.252151]

### Changes
- HTML Error Mapping Changes
- application.properties (Small Setup for MySQL)
- pom.xml (Small Setup for MySQL)

## [04.14.250020]

### Changes
- Faculty Evaluation
- Faculty Evaluation Form
- Cleared the Notifications
- Font Changes
- Icon Changes
- (Temporary ) DELETED separate files of CSS

## [04.13.252106]

### Changes
- Notification Dropdown

### Added
- HTML for error mapping

## [04.12.251227]

### Added
- Javascript codes to login page

## [04.12.250020]

### Fixed
- Header and Main Navigation Gap UI Fixed

## [04.11.251857]

### Fixed
- Font Changes in some pages

## [04.11.251152]

### Fixed
- Icon Changes

## [04.05.251251]

### Fixed
- Minor HTML chages

## [04.06.250020]

### Changes
- Log In Page Container Styles

## [04.05.252213]

### Fixed
- Fixed Profile icon is not accessible to all pages
- FIxed Notification icon is not accessible to all pages

## [04.05.252009]

### Fixed
- Formatted lines of code.

### Changes
- Home Page UI Major Rework

## [04.05.251553]

### Changes
- Separated the content of home.css to multiple files

## [04.05.251510]

### Fixed
- Faculty Evaluation Form Page not having CSS Designs

### BUGS
- Profile icon is not accessible to all pages
- Notification icon is not accessible to all pages

## [04.05.250049]

### REVERT BACK TO VERSION [04.04.251327]

### BUGS
- Profile icon is not accessible to all pages
- Notification icon is not accessible to all pages
- Faculty Evaluation Form Page not having CSS Designs

## [04.04.252337]

### REVERT BACK TO VERSION [04.04.251059]

## [04.04.251327]
### Added
- Interactable Profile Icon
    - Profile Page
- Interactable Notification Icon
    - Notification Dropdown Panel

### Changed
- Grades Page
    - Shows Last Semestral Grades

### Bugs
- The profile icon is not accessible to all pages
- The notification icon is not accessible to all pages
- others

## [04.04.251059]
### Added
- Faculty Evaluation Form.

### Fixed
- Faculty Evaluation Page No Mapping.

### Changed
- Faculty Evaluation Page UI
- Log In Controller
    - Added a Separate Controller for Home

## [04.02.251609]
### Initial Usable Release
- Log in Page.

- Home Page.
    -Announcements, Events, Contacts Page.
- Schedule Page.
- Grades Page.
- Enrollment Page.
- Faculty Evaluation Page.

- Implemented a Log Out Feature.

- Temporary Forgot Password Page.



## [April.1.2025]
- Improved Log In Page UI
- Basic HTML Dashboard after Sign In
- Basic HTML Log Out Feature on Dashboard

## [March.31.2025]
- Coded Log In Page

## [March.30.2025]
- Log In Page UI 
## Coding guidelines:

#### Strings:

| item               | example                             |
|--------------------|-------------------------------------|
| Toolbar title text | login_screen_title                  |
| Hint label text    | login_screen_email_label            |
| Button text        | login_screen_register_button_text   |
| Error text         | login_screen_error_invalid_password |


#### Layouts:

| item           | example              |
|----------------|----------------------|
| Activity view  | activity_main        |
| Fragment view  | fragment_login       |
| List item view | list_item_daily_user |

#### Data bindings:

| item                      | example                     |
|---------------------------|-----------------------------|
| File name                 | LoginScreenBindingsAdapters |
| Binding error to the view | bindLoginEmailError         |
| Binding view visibility   | bindLoginProgressVisibility |
| Binding adapter to list   | bindProjectListAdapter      |

#### Classes:

| item         | example                   |
|--------------|---------------------------|
| Activity     | MainActivity              |
| Fragment     | DailyFragment             |
| List adapter | DailyListAdapter          |
| Use case     | GetLoggedUserUseCase      |
| Repository   | FirebaseProjectRepository |

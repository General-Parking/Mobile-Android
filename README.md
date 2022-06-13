# GeneralParking

## Пользовательская документация

**General Parking** - система умного паркинга, которая с помощью мобильного приложения дает жителям мегаполисов возможность
пользоваться частным крытым паркингом, как городским. В трех словах: **Агрегатор частных парковок**. Основные этапами являются поиск,
бронирование места, автоматический въезд и выезд в зону парковки с использованием методов распознавания номеров.Концепция "заехал-выехал": без касс, паркоматов, чеков и карточек.

### Основная функциональность 
- Регистрация 
- Авторизация
- Личный аккаунт
- Бронирование парковочных мест
- Процесс взаимодействия с паркингом
- Оплата сервиса

### Скрины
<table>
<tr>
<td align="center">Авторизация</td>
<td align="center">Регистрация личных данных</td>
<td align="center">Регистрация ТС</td>
</tr>
<tr>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416907-9fe3ac57-8a6c-478e-aeb1-0228055657cc.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416926-7aec019c-6cee-40ec-931b-2fc55b9a4a01.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416921-4c4306ec-0582-427b-8815-88bb116e4426.jpeg"></td>
</tr>
</table>

<table>
<tr>
<td align="center">Личный кабинет</td>
<td align="center">Оплата</td>
<td align="center">Выбор способа оплаты</td>
</tr>
<tr>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416928-732e5162-22f4-478c-a735-32a60d691e76.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416930-47a50167-adef-4764-9417-97a26c5b8e02.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173417975-2ebb8d05-84e5-4ae8-ac69-fd1e843a2608.jpeg"></td>
</tr>
</table>

<table>
<tr>
<td align="center">Карта</td>
<td align="center">Удержание брони</td>
<td align="center">На парковке</td>
</tr>
<tr>
  <td><img src="https://user-images.githubusercontent.com/54765046/173416918-f04aeef5-3aff-4474-8291-601d6c9c15e9.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173420788-121b819e-7301-435e-a5ae-9d3a6f6f2b4b.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173420781-70c87539-9063-416c-b4aa-72bcaff40e63.jpeg"></td>
</tr>
</table>

<table>
<tr>
<td align="center">Заезд на парковку</td>
<td align="center">Выезд с парковки</td>
<td align="center">Сброс пароля</td>
</tr>
<tr>
  <td><img src="https://user-images.githubusercontent.com/54765046/173421256-d601762e-b42b-4345-b0ed-3345bb5c9faf.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173421268-b1e2d242-f72b-4754-a61f-f00531497477.jpeg"></td>
  <td><img src="https://user-images.githubusercontent.com/54765046/173421277-4bf25740-c1ad-4d88-a38b-873213ef908d.jpeg"></td>
</tr>
</table>


## Документация разработчика

- Минимальное sdk - 26

### Используемый стек и подходы

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM](https://ru.wikipedia.org/wiki/Model-View-ViewModel)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Dagger](https://dagger.dev/)
- [Firebase](https://firebase.google.com/)
- и много всего другого... 

# Полезные ссылки проекта General Parking

[Эмулятор заезда и выезда из паркинга для тестовой БД](https://github.com/IvanLyutak/TestEmulators)

[Эмулятор заезда и выезда из паркинга для основной БД](https://github.com/IvanLyutak/Emulators)

[Документация пользователя базы данных](https://docs.google.com/document/d/1DQVeUMXectKqXcjGpcQl4quEwg8TSSHiw9SurNDWKJg/edit)

[Презентация проекта](https://docs.google.com/presentation/d/16-eHbvozZKoFUfgVn2WLcvX_8upM2R1_eMdlzpbycws/edit?usp=sharing)

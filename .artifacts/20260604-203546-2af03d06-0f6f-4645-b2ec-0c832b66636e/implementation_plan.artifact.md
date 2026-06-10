# Refactor MainActivity to Clean Code

Refactor the existing `MainActivity.kt` to follow Clean Code principles, specifically the Single Responsibility Principle, by decomposing the large file into modular components, screens, and a ViewModel.

## Proposed Changes

### [Core/Refactoring]

Splitting the monolith into smaller, manageable pieces.

#### [NEW] [ConversionUtils.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/util/ConversionUtils.kt)
- Move `convertUniversal` and byte conversion logic here.

#### [NEW] [Screen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/navigation/Screen.kt)
- Define a sealed class for navigation routes.

#### [NEW] [MainViewModel.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/viewmodel/MainViewModel.kt)
- Hold all UI state (`inputValue`, `inputBase`, `riwayatList`, `dataList`).
- Handle business logic like saving history and loading data.

#### [NEW] [NavGraph.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/navigation/NavGraph.kt)
- Define the `AppNavigation` Composable here.

#### [NEW] [DashboardScreen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/screen/DashboardScreen.kt)
#### [NEW] [PenerjemahTeksScreen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/screen/PenerjemahTeksScreen.kt)
#### [NEW] [DaftarBilanganScreen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/screen/DaftarBilanganScreen.kt)
#### [NEW] [DetailBilanganScreen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/screen/DetailBilanganScreen.kt)
#### [NEW] [RiwayatScreen.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/screen/RiwayatScreen.kt)

#### [NEW] [MenuCard.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/component/MenuCard.kt)
#### [NEW] [ResultCard.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/component/ResultCard.kt)
#### [NEW] [BilanganListItem.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/ui/component/BilanganListItem.kt)

#### [MainActivity.kt](file:///D:/Codes/Kotlin/BilanganKu/app/src/main/java/com/example/bilanganku/MainActivity.kt)
- Simplify to only set content and initialize navigation.

---

### [UI/CleanUp]

#### Address IDE Warnings
- Replace `Icons.Default.ArrowBack` with `Icons.AutoMirrored.Filled.ArrowBack`.
- Remove unused exception variables.
- Use `toColorInt()` extension.
- Use `Duration` for `delay` if applicable or keep as is if within standard Compose `LaunchedEffect`.

## Verification Plan

### Automated Tests
- Run `gradle build` to ensure no compilation errors.

### Manual Verification
- Deploy the app and verify all screens are working as before:
    - Navigation between Dashboard, Konversi, Sandi Teks, and Riwayat.
    - Inputting values in Konversi and seeing results.
    - Translating text in Sandi Teks.
    - Saving to history and viewing it.

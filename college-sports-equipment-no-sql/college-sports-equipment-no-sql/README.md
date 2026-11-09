# College Sports Equipment Management — No SQL Version

This version uses Java 17, Spring Boot, HTML, CSS and JavaScript. It does NOT use MySQL, SQL, JDBC, JPA or a database server.

## How data is saved
The backend stores equipment and issue/return history in `data/sports-data.json` in the project working directory. The file is created automatically when the first record is added. Data remains after restarting the application, as long as this file is kept.

## Requirements
- Java JDK 17 or newer
- Maven
- Browser
- Internet connection only if Maven needs to download dependencies

## Run
1. Extract the ZIP file.
2. Open the extracted `college-sports-equipment-no-sql` folder in VS Code or IntelliJ.
3. Open a terminal in that folder.
4. Run `mvn spring-boot:run`.
5. Open `http://localhost:8080`.

No SQL password or database setup is required.

## Features
- Add equipment name, sport and total quantity.
- Issue equipment to a student and update available stock.
- Record partial or full returns.
- Mark some or all returned units as damaged.
- Damaged units are counted separately and never become available stock.
- View issue/return history and pending quantities.
- View sport-wise available, issued and damaged totals.

## API
- GET `/api/equipment`
- POST `/api/equipment` with `{"name":"Football","sport":"Football","quantity":10}`
- POST `/api/issue` with `{"equipmentId":1,"studentName":"Ravi","studentRollNo":"22A01","quantity":2}`
- POST `/api/return/1` with `{"quantity":2,"damagedQuantity":1}`
- GET `/api/history`
- GET `/api/summary`

This is a simple local college-project implementation. Back up `data/sports-data.json` regularly. For production use, add authentication, role-based permissions, stronger validation and atomic file-write/backup handling.

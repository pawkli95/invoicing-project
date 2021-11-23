import { EmailValidator } from "@angular/forms";
import { Role } from "./role";

export interface UserDto {
    id: string,
    username: string,
    password: string,
    firstName: string,
    lastName: string,
    role: Role
    registrationDate: string
}
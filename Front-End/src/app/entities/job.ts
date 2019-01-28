import { User } from "./user";

export class Job {
    id : number;
    creator : User;
    applicants : User[];
    title: string
    skills : string[];
    description : string;
}

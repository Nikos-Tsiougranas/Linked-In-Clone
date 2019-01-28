import { User } from './user';
import { Article } from '../entities/article';

export class MyLike {
    status: string;
    id: number;
    user: User;
    article: Article;
}
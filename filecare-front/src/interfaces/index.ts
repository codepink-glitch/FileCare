export interface UserDetails {
    username: string,
    password: string
}

export interface FileDetails {
    extension: string,
    name: string,
    path: string
}

export enum PopupType {
    UPLOAD_FILE = 'file',
    CREATE_FOLDER = 'folder',
    DELETE = 'delete'
}
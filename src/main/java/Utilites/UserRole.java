/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Utilites;

/**
 *
 * @author ADMIN
 */
public enum UserRole {
    ADMIN("admin"),REGUSER("regUser");
    
    private final String role;
    private UserRole(String role){
        this.role = role;
    }
    @Override
    public String toString(){
        return role;
    }
}

import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  standalone: false,
})
export class NavbarComponent implements AfterViewInit, OnInit {

  currentUsername: string | null = null;


  showLoginAndSignUp = true;

  showLogOut = false;


  rol: string | null = null;


  menuOpen = false;


  loginOpen = false;


  createOpen = true;


  showMenu = false;


  renderMenu = false;


  menuClass = 'ul animate__animated animate__bounceInDown ';

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }








  }

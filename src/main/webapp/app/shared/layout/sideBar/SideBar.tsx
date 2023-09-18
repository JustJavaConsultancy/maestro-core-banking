import React, {useState} from 'react';
import {Link, NavLink} from 'react-router-dom';
import {SidebarNav} from 'app/modules/dashboard/components/SidebarNavs';

export default function SideBar(props) {
    const [activeLink, setActiveLink] = useState(0);

    return(
        <div style = {{position:'fixed', paddingTop: '30px'}}>
            <NavLink isActive = {(match, location) => {
                if(match) return false
                setActiveLink(0); return true
            }} activeClassName = 'sideNavActive' to = '/dash'>
                <SidebarNav active = {activeLink === 0} nav = 'Overview'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(1); return true
            }} activeClassName = 'sideNavActive' to = '/customers'>
                <SidebarNav active = {activeLink === 1} nav = 'Customers'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(2); return true
            }} activeClassName = 'sideNavActive' to = '/dwallets'>
                <SidebarNav active = {activeLink === 2} nav = 'Wallets'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(3); return true
            }} activeClassName = 'sideNavActive' to = '/alltransactions'>
                <SidebarNav active = {activeLink === 3} nav = 'Transactions'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(4); return true
            }} activeClassName = 'sideNavActive' to = '/reports'>
                <SidebarNav active = {activeLink === 4} nav = 'Reports'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(5); return true
            }} activeClassName = 'sideNavActive' to = '/health'>
                <SidebarNav active = {activeLink === 5} nav = 'Service Uptime'/>
            </NavLink>
            <NavLink isActive = {(match, location) => {
                if(!match) return false
                setActiveLink(6); return true
            }} activeClassName = 'sideNavActive' to = '/verifyRRR'>
                <SidebarNav active = {activeLink === 6} nav = 'Verify RRR'/>
            </NavLink>
     </div>
    )
}
import React from 'react';

interface SideBarNav {
  active?: boolean;
  nav?: string;
  top?: boolean
}

export const SidebarNav = ({ active, nav, top }: SideBarNav): JSX.Element => {
  return (
    <div>
      <p
        style={{ 
          width: '90%', 
          color: active ? '#435FAA' : '#000000', 
          fontWeight: active ? 700 : 500, 
          fontSize: '13px',
          lineHeight: '20px',
        }}
      >
      {nav}
      </p>
    </div>
  );
};

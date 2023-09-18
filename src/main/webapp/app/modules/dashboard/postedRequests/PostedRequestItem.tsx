import React, { useState, useEffect } from 'react';
import moment from 'moment';
import Axios from 'axios';

import { CurrentPostType, CheckedValueType } from './PostedRequest';
import CheckBox from '../components/checkBox/CheckBox';
import Approval from '../../../../content/images/approval_green.svg';
import Decline from '../../../../content/images/decline_red.svg';

interface RequestItemType {
    post: CurrentPostType;
    postHandler: (post:CurrentPostType) => void;
    getData: (data:CheckedValueType) => void;
    selected: boolean;
    resetChecked: boolean;
    insertSingle;
    setApproveShow
}

const PostedRequestItem = ({ post, postHandler, getData, selected, resetChecked, insertSingle, setApproveShow }: RequestItemType):JSX.Element => {
    const [checked, setChecked] = useState(false);
    const [hovered, setHovered] = useState(false)

    const handleChange = () => {
       setChecked(!checked);
       getData({
           id:post.id,
           checked: !checked
       })
    }
    
    const handleChecked = () => {
        if(post.status === 'NEW') setChecked(selected);
    }

    useEffect(() => {
        handleChecked()
        // if(!resetChecked){
        //     setChecked(false);
        // }
    }, [selected, resetChecked])

    return ( 
        <tr 
            key={post.id} 
            className="request__row" 
            style={{backgroundColor: checked ? '#C5DAF9' : '#F9F9F9'}}
            onMouseOver={() => {
                if(!selected && checked){
                    setHovered(true)
                }
            }}
            onMouseLeave={() => setHovered(false)}
        >
            <td>
                {post.status === 'NEW' && <CheckBox handleChange={handleChange} checked={checked} />}
            </td>
            <td  onClick={() => postHandler(post)}>{post.user.id}</td>
            <td  onClick={() => postHandler(post)}>{post.audience.code}</td>
            <td  onClick={() => postHandler(post)}>{post.school.name}</td>
            <td colSpan={3}  onClick={() => postHandler(post)}>
                <strong>{post.title}</strong> <span className="title__content__divide">.</span>
                {post.content.substring(0, 50)}...
            </td>
            <td  onClick={() => postHandler(post)}>
                <span
                    style={{
                        padding: '5px 10px',
                        borderRadius: '20px',
                        backgroundColor: post.status === 'NEW' ? 'orange' : post.status === 'APPROVED' ? 'green' : 'red',
                        color: '#fff'
                    }}
                >{post.status}</span>
            </td>
            {!hovered ?
            <td  onClick={() => postHandler(post)}>{moment(post.datePosted).format('ll')}</td>
            : <td>
                <div className="table__approval">
                    <img 
                        src={Approval} 
                        alt="approval icon" 
                        onClick={() => {
                            insertSingle(post)
                            setApproveShow(true)
                        }}
                    />
                    <img src={Decline} alt="approval icon" />
                </div>
            </td>
            }
        </tr>
     );
}
 
export default PostedRequestItem;
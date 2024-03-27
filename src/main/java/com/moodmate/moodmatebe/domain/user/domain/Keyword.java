package com.moodmate.moodmatebe.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Keyword {
    인기짱("인기짱"),
    고양이상("고양이상"),
    강아지상("강아지상"),
    혼자가_좋아("혼자가 좋아"),
    감성충만("감성충만"),
    상상력왕("상상력왕"),
    방콕러("방콕러"),
    반려인("반려인"),
    배려왕("배려왕"),
    쩝쩝박사("쩝쩝박사"),
    몸짱("몸짱"),
    얼굴천재("얼굴천재"),
    패셔니스타("패셔니스타"),
    프로게이머("프로게이머"),
    트렌드세터("트렌드세터"),
    섹시도발("섹시도발"),
    마이웨이("마이웨이");

    private final String keyword;
}
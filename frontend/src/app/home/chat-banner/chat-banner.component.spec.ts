import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatBannerComponent } from './chat-banner.component';

describe('ChatBannerComponent', () => {
  let component: ChatBannerComponent;
  let fixture: ComponentFixture<ChatBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatBannerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

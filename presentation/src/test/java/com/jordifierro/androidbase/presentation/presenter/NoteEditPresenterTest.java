package com.jordifierro.androidbase.presentation.presenter;

import com.jordifierro.androidbase.data.net.error.RestApiErrorException;
import com.jordifierro.androidbase.domain.entity.NoteEntity;
import com.jordifierro.androidbase.domain.interactor.note.GetNoteUseCase;
import com.jordifierro.androidbase.domain.interactor.note.UpdateNoteUseCase;
import com.jordifierro.androidbase.presentation.view.NoteEditView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class NoteEditPresenterTest {

    @Mock GetNoteUseCase getNoteUseCase;
    @Mock UpdateNoteUseCase updateNoteUseCase;
    @Mock NoteEditView mockNoteEditView;
    @Mock Observable mockObservable;

    private NoteEditPresenter noteEditPresenter;
    private NoteEditPresenter.GetNoteSubscriber getNoteSubscriber;
    private NoteEditPresenter.UpdateNoteSubscriber updateNoteSubscriber;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.noteEditPresenter = new NoteEditPresenter(this.updateNoteUseCase, this.getNoteUseCase);
        this.noteEditPresenter.initWithView(this.mockNoteEditView);
        this.getNoteSubscriber = this.noteEditPresenter.new GetNoteSubscriber();
        this.updateNoteSubscriber = this.noteEditPresenter.new UpdateNoteSubscriber();
    }

    @Test
    public void testDestroy() {

        this.noteEditPresenter.destroy();

        verify(this.getNoteUseCase).unsubscribe();
        verify(this.updateNoteUseCase).unsubscribe();
    }

    @Test
    public void testGetNote() throws Exception {

        verify(this.mockNoteEditView).getNoteId();
        verify(this.mockNoteEditView).showLoader();
        verify(this.getNoteUseCase).setParams(any(int.class));
        verify(this.getNoteUseCase).execute(any(NoteEditPresenter.GetNoteSubscriber.class));
    }

    @Test
    public void testGetSubscriberOnCompleted() {

        this.getNoteSubscriber.onCompleted();

        verify(this.mockNoteEditView).hideLoader();
    }

    @Test
    public void testGetSubscriberOnError() {

        this.getNoteSubscriber.onError(new RestApiErrorException("Error message", 500));

        verify(this.mockNoteEditView).hideLoader();
        verify(this.mockNoteEditView).showError(any(String.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetSubscriberOnNext() {

        this.getNoteSubscriber.onNext(new NoteEntity(1, "", ""));

        verify(this.mockNoteEditView).showLoader();
        verify(this.mockNoteEditView).hideLoader();
        verify(this.mockNoteEditView).showNote(any(NoteEntity.class));
    }

    @Test
    public void testUpdateNote() {

        this.noteEditPresenter.updateNote("", "");

        verify(this.mockNoteEditView, atLeast(1)).getNoteId();
        verify(this.mockNoteEditView, atLeast(1)).showLoader();
        verify(this.updateNoteUseCase).setParams(any(NoteEntity.class));
        verify(this.updateNoteUseCase).execute(any(NoteEditPresenter.UpdateNoteSubscriber.class));
    }

    @Test
    public void testUpdateSubscriberOnCompleted() {

        this.updateNoteSubscriber.onCompleted();

        verify(this.mockNoteEditView).hideLoader();
    }

    @Test
    public void testUpdateSubscriberOnError() {

        this.updateNoteSubscriber.onError(new RestApiErrorException("Error message", 500));

        verify(this.mockNoteEditView).hideLoader();
        verify(this.mockNoteEditView).showError(any(String.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateSubscriberOnNext() {

        this.updateNoteSubscriber.onNext(new NoteEntity(1, "", ""));

        verify(this.mockNoteEditView).showLoader();
        verify(this.mockNoteEditView).hideLoader();
        verify(this.mockNoteEditView).close();
    }

}
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEattribut } from '../eattribut.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../eattribut.test-samples';

import { EattributService } from './eattribut.service';

const requireRestSample: IEattribut = {
  ...sampleWithRequiredData,
};

describe('Eattribut Service', () => {
  let service: EattributService;
  let httpMock: HttpTestingController;
  let expectedResult: IEattribut | IEattribut[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EattributService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Eattribut', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eattribut = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eattribut).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Eattribut', () => {
      const eattribut = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eattribut).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Eattribut', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Eattribut', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Eattribut', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEattributToCollectionIfMissing', () => {
      it('should add a Eattribut to an empty array', () => {
        const eattribut: IEattribut = sampleWithRequiredData;
        expectedResult = service.addEattributToCollectionIfMissing([], eattribut);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eattribut);
      });

      it('should not add a Eattribut to an array that contains it', () => {
        const eattribut: IEattribut = sampleWithRequiredData;
        const eattributCollection: IEattribut[] = [
          {
            ...eattribut,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEattributToCollectionIfMissing(eattributCollection, eattribut);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Eattribut to an array that doesn't contain it", () => {
        const eattribut: IEattribut = sampleWithRequiredData;
        const eattributCollection: IEattribut[] = [sampleWithPartialData];
        expectedResult = service.addEattributToCollectionIfMissing(eattributCollection, eattribut);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eattribut);
      });

      it('should add only unique Eattribut to an array', () => {
        const eattributArray: IEattribut[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eattributCollection: IEattribut[] = [sampleWithRequiredData];
        expectedResult = service.addEattributToCollectionIfMissing(eattributCollection, ...eattributArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eattribut: IEattribut = sampleWithRequiredData;
        const eattribut2: IEattribut = sampleWithPartialData;
        expectedResult = service.addEattributToCollectionIfMissing([], eattribut, eattribut2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eattribut);
        expect(expectedResult).toContain(eattribut2);
      });

      it('should accept null and undefined values', () => {
        const eattribut: IEattribut = sampleWithRequiredData;
        expectedResult = service.addEattributToCollectionIfMissing([], null, eattribut, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eattribut);
      });

      it('should return initial array if no Eattribut is added', () => {
        const eattributCollection: IEattribut[] = [sampleWithRequiredData];
        expectedResult = service.addEattributToCollectionIfMissing(eattributCollection, undefined, null);
        expect(expectedResult).toEqual(eattributCollection);
      });
    });

    describe('compareEattribut', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEattribut(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEattribut(entity1, entity2);
        const compareResult2 = service.compareEattribut(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEattribut(entity1, entity2);
        const compareResult2 = service.compareEattribut(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEattribut(entity1, entity2);
        const compareResult2 = service.compareEattribut(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
